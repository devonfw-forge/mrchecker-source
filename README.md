MrChecker template
-------------------

This repository includes a template to make test using MrChecker and an example.

## How to use

To use this template the following steps have to be followed:

* Download the template.

* Import it as Maven project using and IDE like eclipse (incorporated in devonfw).

* Write the code to test an application.

## What is included?

* *common/data:* Directory to store files with java objects.

* *common/mapper:* Includes a csv mapper.

* *common/utils:* Includes a configuration reader and a file with different utilities.

* *pages:* Contains a template (MainPage.java) with methods necessary to build a basic page object.

* *test/cucumber:* Folder to store the cucumber tests (MainCucumberTest.java).

* *test/cucumber/features:* Directory to keep cucumber files with features (MainCucumberFeatures.features).

* *test/junit:* Folder which includes a java file (MainTest.java) with methods recommended to build a basic test.

The example folder also has a the following tests: 

* *Data Driven Behaviour (DDB): * Inside test/junit.

* *Test Driven Development (TDD): * Situated on test/cucumber.
