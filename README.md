[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fdevonfw%2Fdevonfw-testing.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fdevonfw%2Fdevonfw-testing?ref=badge_shield)
[![Maven Central](https://img.shields.io/maven-central/v/com.capgemini.mrchecker/mrchecker-core-module)](https://mvnrepository.com/artifact/com.capgemini.mrchecker)

![MrChecker.png](resources%2Fimg%2FMrChecker.png)

Java E2E Testing Framework accelerating tests creation and reducing maintenance costs

# Modules #

Our testing framework consists of multiple modules you can use depending on your needs. 
* Core (base functionality and dependency for all other modules)
* Selenium
* Playwright
* Web API (with RestAssured)
* CLI
* Database
* Mobile (with Appium)


![Modules.PNG](resources%2Fimg%2FModules.PNG)

# Maven dependency #

Each of module can be used as a Maven dependency. \
You can check list of all related Maven artifacts here:
https://mvnrepository.com/artifact/com.capgemini.mrchecker

As an example if you want to use Selenium module in your project you must add it as a dependency to your pom.xml file:

> &lt;!-- https://mvnrepository.com/artifact/com.capgemini.mrchecker/mrchecker-selenium-module --&gt; \
 &lt;dependency&gt; \
    &lt;groupId&gt;com.capgemini.mrchecker&lt;/groupId&gt; \
    &lt;artifactId&gt;mrchecker-selenium-module&lt;/artifactId&gt; \
    &lt;version&gt;*VERSION&lt;/version&gt; \
 &lt;/dependency&gt;

* VERSION can be found on [releases page](https://github.com/devonfw-forge/mrchecker-source/releases)

# Releases #

Visit [releases page](https://github.com/devonfw-forge/mrchecker-source/releases) to check newest version and related changes.

# Examples #

Visit [examples](https://github.com/devonfw/mrchecker) for some use cases you can use to start your own project

## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fdevonfw%2Fdevonfw-testing.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fdevonfw%2Fdevonfw-testing?ref=badge_large)