# InstantAppSample
An Android Instant App is a native Android app reachable through a URL with no Google Play Store installation required. A piece of the app containing the selected feature is actually downloaded and installed on the fly.

This allows to use the power of the internet to make Android apps much more reachable and shareable.
Checkout the [official website](https://developer.android.com/topic/instant-apps/overview.html) to get an overview of Android Instant Apps.

This sample is aiming to show how to transform a regular native Android application into an Instant Application.


# Setup
These are the prerequisites that you will need in order to start and run the project.

## AndroidStudio
First you need to download and install Android Studio 3.0 (at this time this is still a preview, you can install it next to the stable version, [go here to download the preview](https://developer.android.com/studio/preview/index.html)).

You also need to install the Intant App SDK, as well as the Android "O" SDK and Tools in the SDK Manager (**Tools** > **Android** > **SDK Manager**).
![downloading Instant App SDK](/screenshots/downloadInstantAppSdk.jpg?raw=true)
![downloading API 26 SDK](/screenshots/downloadAndroidO.jpg?raw=true)

Finally you need to setup a device to test instant apps. You can either use a real device from this list (**Nexus 5X**, **Nexus 6P**, **Pixel**, **Pixel XL**, **Galaxy S7** running **Android 6.0** or higher) or use a **Nexus 5X emulator** running **Android 6.0** (API level 23) x86 with Google APIs (x86_64 architectures of the OS on the emulator cannot be used).
Make sure the device or emulator is setup with a Google account. To enable instant apps go to the **Settings** app and then **Google** > **Instant Apps**.

You can review all the steps for the setup on the [official site](https://developer.android.com/topic/instant-apps/getting-started/setup.html).

## Running the project

First of all, clone the project:

```git clone https://github.com/jbvincey/InstantAppSample.git```

Then open with Android Studio 3 (Open an existing Android Studio project), checkout ```develop_installed_app``` branch and run the project. This sample is composed of 2 activities, the first one showing a list of vacation trips, the second one giving the details of a trip. The project is based on a Model-View-Presenter architecture. Android Studio might ask you to update the gradle version (gradle plugin 3 was still in beta at the time this project was developed).

Now you can try to run the project on a device or emulator (beware that minimal Android SDK version is 23).
![list of trips](/screenshots/sampleListOfTrips.jpg?raw=true)
![trips details](/screenshots/sampleTripDetails.jpg?raw=true)

# Moving to a single feature module App

## Feature modules

Feature modules are the base of the Instant Apps architecture. Basically, a feature module will implement a feature that is addressable through a URL and therefore can be run as an instant app. Usually an application will be split into several feature modules, each of them giving access to a feature of the application reachable through a URL. 

Feature modules work in 2 different ways:
* for a standard application it generates an aar and works like a library
* for an instant application it generates an Instant App APK

Therefore, both instant app APK and standard app APK (to be uploaded on the Google Play Store) are based on the same source code, so that you have only one project for your instant app and standard app.

An important aspect of feature modules is that it cannot exceed 4MB. This seems a tough limitation for developers, but we have to keep in mind that feature modules are downloaded and installed on the fly when clicking on the corresponding URL. In order to cope with this, you could also create feature modules only for the features that you want to make accessible as Instant App, and have a regular module for all other features that will only be available on the Play Store application.

There is actually a special type of feature module called *base feature* module. The base feature will be loaded whenever an instant app feature is started. Therefore a base feature module will usually contain all the common code required by different features. There can only be one base feature module. A typical instant app project will include one base feature module and several feature modules.

## From existing code to single feature module project

### Converting current module into a base feature module

The goal of this step is to convert a standard application project into an instant app project with one feature. You can try it yourself based on the following instructions, starting from the ```develop_installed_app``` git branch. You could also see the result on the ```develop_instant_monofeature``` git branch.

To achieve this, we will convert the existing *app* module into a *base feature* module, and then add a basic *application* module as well as an *instant app* module that will only depend on the base feature module. 

First checkout ```develop_installed_app```, rename the ```app``` module into ```instanttripbase``` (**right-click** on ```app``` module > **Refactor** > **Rename**).

Next, in order to make it a *feature* module instead of an *application* module,  in **instranttripbase/build.gradle** replace ```com.android.application``` with ```com.android.feature```:
```gradle
apply plugin: 'com.android.feature'
```

In the same file remove the ```applicationId``` declaration (*applicationId* should only be declared in the *application* module).

Finally, to specify that this is a *base* feature module, add ```baseFeature = true``` under ```android``` (still in this gradle file):
```gradle
android {
    ...
    baseFeature = true
    ...
}
```

### Adding the application module

Now it is time to create the *application* module. Click on **File** > **New** > **New Module**, then choose **Phone & Tablet Module**. Choose a name for this module (for example *InstantTrip APK*), and make sure to have a different package name from the base feature module. Beware not to put a dash (-) in any module name as it could lead to compilation problems. Finally check the minimum Android API version (should be API 23 Android 6.0 in this project).
![creating app module](/screenshots/appModuleCreation.jpg?raw=true)

In the next step, select **Add No Activity** and click **Finish**.

In the gradle file of your new module (e.g. **instanttripapk/build.gradle**) remove every dependencies and place ```implementation project(':instanttripbase')``` instead.

```gradle
dependencies {
    implementation project(':instanttripbase')
}
```

We will then remove all unused files from this module. Switch to *Project* view

![switching to project view](/screenshots/projectView.jpg?raw=true)

and remove the *androidTest* and *test* folders from your new app module, as well as *main/java* and *main/res* folders.

![removing unused folders](/screenshots/appModuleDeleteUnusedFolders.jpg?raw=true)

Finally open the Android manifest from this module (e.g. **instanttripapk/src/main/AndroidManifest.xml**) and remove the *application* element. Your manifest should look something like this:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.jbvincey.instantappssample.instanttrip">
</manifest>
```
Make sure to keep the *android* domain declaration in this manifest (be careful since it could be automatically removed if you commit with the *optimize import* option), without this, the merge of manifests might not work correctly.

You can now run a Gradle synchronization, rebuild the project, and try it on an emulator or device, it should work exactly like before.

Notice that you cannot use *Butterknife*, nor put resource identifiers ```R.id``` in switch statements, since these identifiers are not finals (remember that all your code is now in a module). 

### Adding the instant app module

The last step to get an instant app is to create the *instant app* module. This module doesn't contain any code, it is only a wrapper around the feature modules that implement the instant app *features*. Click on **File** > **New** > **New Module**, then choose **Instant App**. Give a name to this module (for example **instanttripinstantapp**).

In the gradle file of your new module (e.g. **instanttripinstantapp/build.gradle**) add the dependency for your feature ```implementation project(':instanttripbase')```.

```gradle
dependencies {
    implementation project(':instanttripbase')
}
```

At this point, the last important step is to associate links (URLs) to our features. In this sample application, there are 2 features:
* the first one is the list of trips available
* the second one the detail view of a trip. 

Android Studio 3 provides a built-in tool for this task. Click on **Tools** > **App Links Assistant** > **Open URL Mapping Editor**.

Here we need to understand the different URL patterns. There are actually 3 path patterns that are used to match URLs and link to the right Activity. The first one is simply called *path*, your URL should exactly match the defined path. The second one is called *pathPrefix*, you only define a prefix and a URL will match if it contains this prefix. The last one is the *pathPattern*, there you can define a path with regexp, that the URL should exactly match.

In the sample, we will use a *path* for the first Activity displaying the list of available trips. In the *URL Mapping Editor*:
* add a URL (+)
* in *Host* enter your domain name (e.g. **yourdomain.com**)
* for the *Path*, first choose **path** and enter **/trips** in the field
* choose **MainActivity** and click **OK**

Start again the very same thing but this time with the scheme **https** (both *http* and *https* URLs should be defined).

For the second Activity, we will now use a *pathPattern* in order to add the trip identifier in the last segment of the URL:
* add a new URL (+)
* write down the same host
* choose **pathPattern** for the *Path* and enter **/trips/.\*** in the field
* choose the **DetailsActivity** and click **OK**

Once again with **https**.

You should have now something like this:
![URL mapping](/screenshots/urlMapping.jpg?raw=true)

The whole URL mapping is now defined in the Android manifest of the base feature module. You could also have done this directly in the manifest without using the **App Links Assistant**, although it is quite convenient and will still be useful in further steps.

Then we need to adapt the DetailsActivity to recover the trip identifier from the last segment of the URL. In **DetailsActivity**, in the method **onCreate()**, replace:
```java
String tripId = getIntent().getStringExtra(KEY_TRIP_ID);
```
with
```java
String tripId = null;
Intent intent = getIntent();
Uri uri = intent.getData();
if (uri != null && uri.getLastPathSegment() != null) {
    tripId = uri.getLastPathSegment();
} else if (intent.hasExtra(KEY_TRIP_ID)) {
    tripId = intent.getStringExtra(KEY_TRIP_ID);
} else {
    showTripLoadingError();
}
```

Now it's time to test our instant app! First sync Gradle and rebuild. Then dropdown the **run configuration** and click on **Edit Configuration**. Create a new **Run Configuration** (+), give a name (for the list activity), select your instant app module (e.g. **instanttripinstantapp**). In *Launch Options* > *Launch* select **URL**, write your URL for the MainActivity (e.g. **https://yourdomain.com/trips**). You should have something like this:
![run configuration](/screenshots/runConfiguration.jpg?raw=true)

Uninstall the app from your emulator or device and run with your new configuration: this should launch the MainActivity as an instant app (you should see the following screen just before the list of trips).
![Instant App loading](/screenshots/instantTripLoading.jpg?raw=true)

We will do the same for the DetailsActivity. Create a new **Run Configuration** with a new name, select your instant app module, choose URL as launch option and this time give a matching URL with a valid trip identifier (you can see the list of trips in assets/trips/trips.json in your base feature module), for example **https://yourdomain.com/trips/3945314588**. Now run with this new configuration, you should directly go to the DetailsActivity with a specific trip (depending on the identifier you chose).

Here it is, you have a running instant app! However, if you click on **location** in the DetailsActivity you will get a crash. The problem is that you are not allowed to specify a package name in an intent in order to launch a specific application (in this case Google Maps). Open the class **IntentHelper** and in the **getMapsLocationIntent()** method, comment the following line:
```java
mapIntent.setPackage(PACKAGE_MAPS);
```

# Multi-feature instant app

In the last section, we managed to create an instant app. However the whole app is contained in only one feature module, which is unlikely to happen in real instant applications. In this section, we will make separate modules to embed our 2 features, the list of trips and the trip details, these 2 feature modules relying on the base feature module we created in the last section. 

## List of trips feature module

Click on **File** > **New** > **New Module** and choose **Feature Module**. Then define a name for this module (for example **InstantTrip List**) and finally choose **Add No Activity**.

![creating list of trips module](/screenshots/tripListModuleCreation.jpg?raw=true)

We will now move some of the code from the base feature module to our new module. Create an **activities** package as well as an **adapters** package, and move **MainActivity** and **TripAdapter** to these. Under **res/** directory create a **layout** folder, and move **activity_main.xml** and **item_trip.xml** from the base module to this new folder. You should now have something like this:

![moving classes to list of trips module](/screenshots/tripListClasses.jpg?raw=true)

In the base feature module manifest, copy the *application* element along with the *MainActivity* element, and paste into the manifest of the list of trips feature module, in order to get something like this (you also need to update the activity's package name):
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.jbvincey.instanttriplist">
    
    <application
        android:name="com.jbvincey.instantappssample.InstantAppSampleApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name="com.jbvincey.instanttriplist.activities.MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data
                    android:host="yourdomain.com"
                    android:path="/trips"
                    android:scheme="http" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data
                    android:host="yourdomain.com"
                    android:path="/trips"
                    android:scheme="https" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```
You should also remove the *MainActivity* element from the base module manifest.

At this step, we need to adapt and clean some code. In **activity_main.xml** update the **MainActivity** package from the ```tools:context``` attribute. In the **MainActivity**, remove the **R** import (e.g. ```com.jbvincey.instantappssample.R```) and import the **R** dependency from the feature module (e.g. ```com.jbvincey.instanttriplist.R```). There is still a problem in the method ```showTripLoadingError```, the string resources are still in the base feature module. To cope with this, simply put the package name of the base module before ```R.string…``` You should get something like this:
```java
@Override
public void showTripLoadingError() { 
    showSnack(com.jbvincey.instantappssample.R.string.trip_list_loading_error);
}
```

Repeat the same in the **TripAdapter** class, remove the **R** import and re-import **R** from the feature module.


In **instanttriplist/build.gradle**, remove all dependencies and put ```api project(':instanttripbase')``` instead:
```gradle
dependencies {
    api project(':instanttripbase')
}
```

Finally we add the new module dependency. In **instanttripapk/build.gradle** and **instanttripinstantapp/build.gradle** add the dependency ```implementation project(':instanttriplist')```. You should have something like this:
```gradle
dependencies {
    implementation project(':instanttripbase')
    implementation project(':instanttriplist')
}
```

In **instanttripbase/build.gradle**, make sure to have ```application project(':instanttripapk')``` and ```feature project(":instanttriplist")``` under dependencies (```feature project(":instanttriplist")``` should already be automatically added, usually at the end of the dependency block):
```gradle
dependencies {
    application project(':instanttripapk')
    feature project(":instanttriplist")
    …
}
```

That's it for the list of trips module! **Clean** and **Rebuild**, you should still be able to launch the app as full application and instant app.

## Trip details feature module

This is the last part of the project transformation. Create the new feature module: **File** > **New** > **New Module** and choose **Feature Module**. Then define a name for this module (for example **InstantTrip Details**) and choose **Add No Activity**.

Create the package **activities** and move **DetailsActivity** to it. Create a **layout** directory under **res**/ and move **activity_detail.xml** to it. In this layout update the package name for **DetailsActivity** in the ```tools:context``` attribute. In **DetailsActivity**, remove the **R** import and re-import **R** from this feature module (e.g. ```com.jbvincey.instanttripdetails.R```). In ```showTripLoadingError()``` and ```confirmBooking()``` methods, add the base module package name before ```R.string…``` and ```R.drawable.asos``` (the drawable resources are also in the base feature module), as you did in **MainActivity**.

In **MainActivity** in the method ```goToTripDetails()```you will notice that you have no reference to **DetailsActivity**, since it is in its own feature module now. Comment the line for now, and we will deal with instant app navigation in the next section.

In the base feature module manifest, copy the *application* element along with the *DetailsActivity* element, and paste into the manifest of the trip details feature module, in order to get something like this (you also need to update the activity's package name):
```xml
 <manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.jbvincey.instanttripdetails">

    <application
        android:name="com.jbvincey.instantappssample.InstantAppSampleApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">

        <activity android:name="com.jbvincey.instanttripdetails.activities.DetailsActivity">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data
                    android:host="yourdomain.com"
                    android:pathPattern="/trips/.*"
                    android:scheme="http" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data
                    android:host="yourdomain.com"
                    android:pathPattern="/trips/.*"
                    android:scheme="https" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

In the base feature module, remove the *DetailsActivity* and the *application* elements. Your base feature module manifest should look something like this:
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.jbvincey.instantappssample">
    
</manifest>
```

In **instantdetails/build.gradle**, remove all dependencies and put ```api project(':instanttripbase')``` instead:
```gradle
dependencies {
    api project(':instanttripbase')
}
```

In **instanttripapk/build.gradle** and **instanttripinstantapp/build.gradle** add the dependency ```implementation project(':instanttripdetails')```. You should have something like this:
```gradle
dependencies {
    implementation project(':instanttripbase')
    implementation project(':instanttriplist')
    implementation project(':instanttripdetails')
}
```

In **instanttripbase/build.gradle**, make sure to have ```application project(':instanttripapk')``` and ```feature project(":instanttripdetails")``` under dependencies (```feature project(":instanttripdetails")``` should already be automatically added, usually at the end of the dependency block):

```gradle
dependencies {
    application project(':instanttripapk')
    feature project(":instanttriplist")
    feature project(":instanttripdetails")
    …
}
```

## Navigation in instant app project

To be continued…
