# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#keep repository related classes and annotations
-keep class com.bruno13palhano.data.di.CharacterRep
-keep class com.bruno13palhano.data.di.CharacterSummaryRep
-keep class com.bruno13palhano.data.di.ComicsRep
-keep class com.bruno13palhano.data.repository.** { *; }

#keep remote data related classes
-keep class com.bruno13palhano.data.remote.** { *; }

#keep local data related classes and annotations
-keep class com.bruno13palhano.data.local.di.DefaultCharacter
-keep class com.bruno13palhano.data.local.di.DefaultCharacterSummary
-keep class com.bruno13palhano.data.local.di.DefaultComic
-keep class com.bruno13palhano.data.local.di.DefaultComicMediator
-keep class com.bruno13palhano.data.local.** { *; }

#keep model related classes
-keep class com.bruno13palhano.data.model.** { *; }