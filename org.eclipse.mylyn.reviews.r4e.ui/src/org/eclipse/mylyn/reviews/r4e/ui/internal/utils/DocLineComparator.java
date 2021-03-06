/*******************************************************************************
 * Copyright (c) 2011, 2012 Ericsson AB and others.
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Description:
 * 
 * This class is adapted from org.eclipse.compare.internal.DocLineComparator
 * and is used to compare lines in two or three-way compares
 * 
 * Contributors:
 *   Sebastien Dubois - Created for Mylyn Review R4E project
 *   
 ******************************************************************************/

package org.eclipse.mylyn.reviews.r4e.ui.internal.utils;

import org.eclipse.compare.contentmergeviewer.ITokenComparator;
import org.eclipse.compare.rangedifferencer.IRangeComparator;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

/**
 * Implements the <code>IRangeComparator</code> interface for lines in a document. A <code>DocLineComparator</code> is
 * used as the input for the <code>RangeDifferencer</code> engine to perform a line oriented compare on documents.
 * <p>
 * A <code>DocLineComparator</code> doesn't know anything about line separators because its notion of lines is solely
 * defined in the underlying <code>IDocument</code>.
 * 
 * @author Sebastien Dubois
 * @version $Revision: 1.0 $
 */
public class DocLineComparator implements ITokenComparator {

	/**
	 * Field fDocument.
	 */
	private final IDocument fDocument;

	/**
	 * Field fLineOffset.
	 */
	private int fLineOffset;

	/**
	 * Field fLineCount.
	 */
	private int fLineCount;

	/**
	 * Field fLength.
	 */
	private int fLength;

	/**
	 * Field fIgnoreWhiteSpace.
	 */
	private final boolean fIgnoreWhiteSpace;

	/**
	 * Creates a <code>DocLineComparator</code> for the given document range. ignoreWhiteSpace controls whether
	 * comparing lines (in method <code>rangesEqual<code>) should ignore whitespace.
	 * 
	 * @param document
	 *            the document from which the lines are taken
	 * @param region
	 *            if non-<code>null</code> only lines within this range are taken
	 * @param ignoreWhiteSpace
	 *            if <code>true</code> white space is ignored when comparing lines
	 */
	public DocLineComparator(IDocument document, IRegion region, boolean ignoreWhiteSpace) {
		fDocument = document;
		fIgnoreWhiteSpace = ignoreWhiteSpace;

		fLineOffset = 0;
		if (null != region) {
			fLength = region.getLength();
			final int start = region.getOffset();
			try {
				fLineOffset = fDocument.getLineOfOffset(start);
			} catch (BadLocationException ex) {
				// silently ignored
			}

			if (0 == fLength) {
				// optimization, empty documents have one line
				fLineCount = 1;
			} else {
				int endLine = fDocument.getNumberOfLines();
				try {
					endLine = fDocument.getLineOfOffset(start + fLength);
				} catch (BadLocationException ex) {
					// silently ignored
				}
				fLineCount = endLine - fLineOffset + 1;
			}
		} else {
			fLength = document.getLength();
			fLineCount = fDocument.getNumberOfLines();
		}
	}

	/**
	 * Returns the number of lines in the document.
	 * 
	 * @return number of lines * @see org.eclipse.compare.rangedifferencer.IRangeComparator#getRangeCount()
	 */
	public int getRangeCount() {
		return fLineCount;
	}

	/* (non Javadoc)
	 * see ITokenComparator.getTokenStart
	 */
	/**
	 * Method getTokenStart.
	 * 
	 * @param line
	 *            int
	 * @return int
	 * @see org.eclipse.compare.contentmergeviewer.ITokenComparator#getTokenStart(int)
	 */
	public int getTokenStart(int line) {
		try {
			final IRegion r = fDocument.getLineInformation(fLineOffset + line);
			return r.getOffset();
		} catch (BadLocationException ex) {
			return fDocument.getLength();
		}
	}

	/* (non Javadoc)
	 * Returns the length of the given line.
	 * see ITokenComparator.getTokenLength
	 */
	/**
	 * Method getTokenLength.
	 * 
	 * @param line
	 *            int
	 * @return int
	 * @see org.eclipse.compare.contentmergeviewer.ITokenComparator#getTokenLength(int)
	 */
	public int getTokenLength(int line) {
		return getTokenStart(line + 1) - getTokenStart(line);
	}

	/**
	 * Returns <code>true</code> if a line given by the first index matches a line specified by the other
	 * <code>IRangeComparator</code> and index.
	 * 
	 * @param thisIndex
	 *            the number of the line within this range comparator
	 * @param otherComparator
	 *            the range comparator to compare this with
	 * @param otherIndex
	 *            the number of the line within the other comparator
	 * @return <code>true</code> if the lines are equal * @see
	 *         org.eclipse.compare.rangedifferencer.IRangeComparator#rangesEqual(int, IRangeComparator, int)
	 */
	public boolean rangesEqual(int thisIndex, IRangeComparator otherComparator, int otherIndex) {

		if (null != otherComparator && otherComparator.getClass().equals(getClass())) {
			final DocLineComparator other = (DocLineComparator) otherComparator;

			if (fIgnoreWhiteSpace) {
				final String s1 = extract(thisIndex);
				final String s2 = other.extract(otherIndex);
				//return s1.trim().equals(s2.trim());
				return compare(s1, s2);
			}

			final int tlen = getTokenLength(thisIndex);
			final int olen = other.getTokenLength(otherIndex);
			if (tlen == olen) {
				final String s1 = extract(thisIndex);
				final String s2 = other.extract(otherIndex);
				return s1.equals(s2);
			}
		}
		return false;
	}

	/**
	 * Aborts the comparison if the number of tokens is too large.
	 * 
	 * @param length
	 *            a number on which to base the decision whether to return <code>true</code> or <code>false</code>
	 * @param maxLength
	 *            another number on which to base the decision whether to return <code>true</code> or <code>false</code>
	 * @param other
	 *            the other <code>IRangeComparator</code> to compare with
	 * @return <code>true</code> to avoid a too lengthy range comparison * @see
	 *         org.eclipse.compare.rangedifferencer.IRangeComparator#skipRangeComparison(int, int, IRangeComparator)
	 */
	public boolean skipRangeComparison(int length, int maxLength, IRangeComparator other) {
		return false;
	}

	//---- private methods

	/**
	 * Extract a single line from the underlying document without the line separator.
	 * 
	 * @param line
	 *            the number of the line to extract
	 * @return the contents of the line as a String
	 */
	private String extract(int line) {
		if (line < fLineCount) {
			try {
				final IRegion r = fDocument.getLineInformation(fLineOffset + line);
				return fDocument.get(r.getOffset(), r.getLength());
			} catch (BadLocationException e) {
				// silently ignored
			}
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Method compare.
	 * 
	 * @param s1
	 *            String
	 * @param s2
	 *            String
	 * @return boolean
	 */
	private boolean compare(String s1, String s2) {
		final int l1 = s1.length();
		final int l2 = s2.length();
		int c1 = 0, c2 = 0;
		int i1 = 0, i2 = 0;

		while (c1 != -1) {

			c1 = -1;
			while (i1 < l1) {
				char c = s1.charAt(i1++);
				if (!Character.isWhitespace(c)) {
					c1 = c;
					break;
				}
			}

			c2 = -1;
			while (i2 < l2) {
				char c = s2.charAt(i2++);
				if (!Character.isWhitespace(c)) {
					c2 = c;
					break;
				}
			}

			if (c1 != c2) {
				return false;
			}
		}
		return true;
	}
}
