# NFCBridge

> Native bridge between the OySy Wallet PWA and Android Beam technology.


## Running this application
In order to build and deploy the application, Android Studio can be used. Import the project and deploy it to a connected Android device. Alternatively, the unsigned APK file, located at `app/build/outputs/apk/debug/app-debug.apk`, can be copied to a device and installed. This requires that the Android phone allows applications from <a href="https://support.google.com/nexus/answer/7391672?hl=en">unknown sources</a> to be installed.

## Using this application
This application can be used to transfer new payment requests on its own. To do so, fill out the form in the application, which is persisted for later uses.
Another way how this transaction data can be set is by starting the app from the 'Request' page of the OySy Wallet. This will automatically pre-fill the form. Note that 'Show Advanced Options' needs to be enabled on the Settings page of the OySy Wallet.
Once the payment information is complete, the transfer process can be initiated by tapping another phone to the hosting android device.

## Running in production
If this application is used in production, set the URL of the OySy wallet according to your setup. Edit the `hostBase` and `basePattern` variables in `app/src/main/java/ch/uzh/csg/nfcbridge/BazoURIScheme.java` and adapt the unit tests accordingly.
