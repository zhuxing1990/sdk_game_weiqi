# sdk_game_weiqi
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	     implementation 'com.github.zhuxing1990:sdk_game_weiqi:1.5'
	}
Share this release:

Tweet      [![](https://jitpack.io/v/zhuxing1990/sdk_game_weiqi.svg)](https://jitpack.io/#zhuxing1990/sdk_game_weiqi)     Link


That's it! The first time you request a project JitPack checks out the code, builds it and serves the build artifacts (jar, aar).

If the project doesn't have any GitHub Releases you can use the short commit hash or 'master-SNAPSHOT' as the version.
