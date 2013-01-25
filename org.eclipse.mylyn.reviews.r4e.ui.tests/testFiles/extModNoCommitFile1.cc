/*******************************************************************************
 * Copyright (c) 2011, 2012 Ericsson AB and others.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 ******************************************************************************/

//This is a test file used by the R4E UI Test framework

class extModNoCommitFile {

public:
	extModNoCommitFile() {}

	void setSomeBool(bool someBool) {
		this.someBoolMod = someBool;
	}

	void addSomeBool() {
		this.someBoolMod += this.someBoolMod;
	}

private:
	bool someBoolMod;
};

int main() {
	extModNoCommitFile test = new extModNoCommitFile();
	test.setSomeBool(1);
	test.addSomeBool();
	delete test;
}
