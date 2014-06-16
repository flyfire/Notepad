Android Platform Develop
==============================
## Setup and Building ##
### Android Build System ###

A makefile defines how to build a particular application. Makefiles typically include all of the following elements:

+ Name: Give your build a name (``LOCAL_MODULE := <build_name>``).
+ Local Variables: Clear local variables with CLEAR_VARS (``include $(CLEAR_VARS)``).
+ Files: Determine which files your application depends upon (``LOCAL_SRC_FILES := main.c``).
+ Tags: Define tags, as necessary (``LOCAL_MODULE_TAGS := eng development``).
+ Libraries: Define whether your application links with other libraries (``LOCAL_SHARED_LIBRARIES := cutils``).
+ Template file: Include a template file to define underlining make tools for a particular target (``include $(BUILD_EXECUTABLE)``).

```bash
LOCAL_PATH := $(my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := <buil_name>
LOCAL_SRC_FILES := main.c
LOCAL_MODULE_TAGS := eng development
LOCAL_SHARED_LIBRARIES := cutils
include $(BUILD_EXECUTABLE)
(HOST_)EXECUTABLE, (HOST_)JAVA_LIBRARY, (HOST_)PREBUILT, (HOST_)SHARED_LIBRARY,
  (HOST_)STATIC_LIBRARY, PACKAGE, JAVADOC, RAW_EXECUTABLE, RAW_STATIC_LIBRARY,
  COPY_HEADERS, KEY_CHAR_MAP
```

-------------------------------

+ ``make eng``
    - Installs modules tagged with: ``eng``, ``debug``, ``user``, and/or ``development``.
    - Installs non-APK modules that have no tags specified.
    - Installs APKs according to the product definition files, in addition to tagged APKs.
    - ``ro.secure=0``
    - ``ro.debuggable=1``
    - ``ro.kernel.android.checkjni=1``
    - ``adb`` is enabled by default.

--------------------------------

+ ``make ueser``
    - Installs modules tagged with user.
    - Installs non-APK modules that have no tags specified.
    - Installs APKs according to the product definition files; tags are ignored for APK modules.
    - ``ro.secure=1``
    - ``ro.debuggable=0``
    - ``adb`` is disabled by default.

--------------------------------

+ ``make userdebug``
    - Also installs modules tagged with ``debug``.
    - ``ro.debuggable=1``
    - ``adb`` is enabled by default.

#### Configuring a new product ####
[Ref](http://www.kandroid.org/online-pdk/guide/build_new_device.html):http://www.kandroid.org/online-pdk/guide/build_new_device.html

#### Build Cookbook ####
[Ref](http://www.kandroid.org/online-pdk/guide/build_cookbook.html):http://www.kandroid.org/online-pdk/guide/build_cookbook.html
+ Building a simple APK
```bash
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

# Build all java files in the java subdirectory
LOCAL_SRC_FILES := $(call all-subdir-java-files)

# Name of the APK to build
LOCAL_PACKAGE_NAME := LocalPackage

# Tell it to build an APK
include $(BUILD_PACKAGE)
```
+ Building a APK that depends on a static .jar file
```bash
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

# List of static libraries to include in the package
LOCAL_STATIC_JAVA_LIBRARIES := static-library

# Build all java files in the java subdirectory
LOCAL_SRC_FILES := $(call all-subdir-java-files)

# Name of the APK to build
LOCAL_PACKAGE_NAME := LocalPackage

# Tell it to build an APK
include $(BUILD_PACKAGE)
```
+ Building a APK that should be signed with the platform key
```bash
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

# Build all java files in the java subdirectory
LOCAL_SRC_FILES := $(call all-subdir-java-files)

# Name of the APK to build
LOCAL_PACKAGE_NAME := LocalPackage

LOCAL_CERTIFICATE := platform

# Tell it to build an APK
include $(BUILD_PACKAGE)
```
+ Building a APK that should be signed with a specific vendor key
```bash
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

# Build all java files in the java subdirectory
LOCAL_SRC_FILES := $(call all-subdir-java-files)

# Name of the APK to build
LOCAL_PACKAGE_NAME := LocalPackage

LOCAL_CERTIFICATE := vendor/example/certs/app

# Tell it to build an APK
include $(BUILD_PACKAGE)
```
+ Adding a prebuilt APK
```bash
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

# Module name should match apk name to be installed.
LOCAL_MODULE := LocalModuleName
LOCAL_SRC_FILES := $(LOCAL_MODULE).apk
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_SUFFIX := $(COMMON_ANDROID_PACKAGE_SUFFIX)

include $(BUILD_PREBUILT)
```
+ Adding a Static Java Library
```bash
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

# Build all java files in the java subdirectory
LOCAL_SRC_FILES := $(call all-subdir-java-files)

# Any libraries that this library depends on
LOCAL_JAVA_LIBRARIES := android.test.runner

# The name of the jar file to create
LOCAL_MODULE := sample

# Build a static jar file.
include $(BUILD_STATIC_JAVA_LIBRARY)
```
+ Android.mk Variables
    * ``LOCAL_`` - These variables are set per-module. They are cleared by the ``include $(CLEAR_VARS)`` line, so you can rely on them being empty after including that file. Most of the variables you'll use in most modules are LOCAL_ variables.
    * ``PRIVATE_`` - These variables are make-target-specific variables. That means they're only usable within the commands for that module. It also means that they're unlikely to change behind your back from modules that are included after yours.
    * ``HOST_`` and ``TARGET_`` - These contain the directories and definitions that are specific to either the host or the target builds. Do not set variables that start with ``HOST_`` or ``TARGET_`` in your makefiles.
    * ``BUILD_`` and ``CLEAR_VARS`` - These contain the names of well-defined template makefiles to include. Some examples are ``CLEAR_VARS`` and ``BUILD_HOST_PACKAGE``.

### Creating Release Keys and Signing Builds ###
Android requires that each application be signed with the developer's digital keys to enforce signature permissions and application request to use shared user ID or target process.
+ ``platform``: a key for packages that are part of the core platform.
+ ``shared``: a key for things that are shared in the home/contacts process.
+ ``media``: a key for packages that are part of the media/download system.
+ ``releasekey``: the default key to sign with if not otherwise specified

These keys are used to sign applications separately for release images and are not used by the Android build system. The build system signs packages with the testkeys provided in ``build/target/product/security/``. Because the testkeys are part of the standard Android open source distribution, they should never be used for production devices. Instead, device manufacturers should generate their own private keys for shipping release builds.

A device manufacturer's keys for each product should be stored under ``vendor/<vendor_name>/security/<product_name>``, where ``<vendor_name>`` and ``<product_name>`` represent the manufacturer and product names. 

```bash
#!/bin/sh
#Filename: mkkey.sh
#Usage:
# sh mkkey.sh platform # enter password
# sh mkkey.sh media # enter password
# sh mkkey.sh shared # enter password
# sh mkkey.sh release # enter password
AUTH='/C=US/ST=California/L=Mountain View/O=Android/OU=Android/CN=Android/emailAddress=android@android.com'
if [ "$1" == "" ]; then
        echo "Create a test certificate key."
        echo "Usage: $0 NAME"
        echo "Will generate NAME.pk8 and NAME.x509.pem"
        echo "  $AUTH"
        exit
fi

openssl genrsa -3 -out $1.pem 2048

openssl req -new -x509 -key $1.pem -out $1.x509.pem -days 10000 \
    -subj "$AUTH"

echo "Please enter the password for this key:"
openssl pkcs8 -in $1.pem -topk8 -outform DER -out $1.pk8 -passout stdin
```

Signing a build for release,Signing a build for a release is a two-step process.
+ Sign all the individual parts of the build. 
+ Put the parts back together into image files.

Use ``build/tools/releasetools/sign_target_files_apks`` to sign a ``target_files`` package. The ``target_files`` package isn't built by default, you need to make sure to specify the "dist" target when you call make.``make -j4 PRODUCT-<product_name>-user dist``.The command above creates a a file under ``out/dist`` called ``<product_name>-target_files.zip``. ``./build/tools/releasetools/sign_target_files_apks -d vendor/<vendor_name>/security/<product_name> <product_name>-target_files.zip signed-target-files.zip``If you have prebuilt and pre-signed apk's in your build that you don't want re-signed, you must explicitly ignore them by adding ``-e Foo.apk=`` to the command line for each apk you wish to ignore.

Creating image files,Once you have signed-target-files.zip, create the images so you can put it onto a device with the command below:``build/tools/releasetools/img_from_target_files signed-target-files.zip signed-img.zip``,``signed-img.zip`` contains all the ``.img`` files. You can use ``fastboot update signed-img.zip`` to use fastboot to get them on the device.

### Customization ###
+ Boot Screen Customization
    * Create a 320x480 image, splashscreen.jpg in this example.
    * Using ImageMagick, convert your ``.jpg`` file to ``.r`` format:``convert screen.jpg screen.r``
    * Use the rgb2565 application to convert the image to 565 format:``rgb2565 < screen.rgb > screen.565``
    * Use fastboot to flash the image to the device:``fastboot flash splash1 screen.565``
+ Network Configuration
Android stores network configurations as a resource that gets compiled into binary at form at build time. The XML representation of this resource is located at ``/android/frameworks/base/core/res/res/xml/apns.xml``. This file does not include any configured APNs.To set the APN configuration for a particular product target, add an ``apns-conf.xml`` file to the product configuration (do not modify the default platform APNs). This allows multiple products, all with different APNs, to be built off the same code base.To configure APNs at the product level, add a line to the product configuration file like the example below (``vendor/<vendor_name>/products/myphone-us.mk``):``PRODUCT_COPY_FILES := vendor/acme/etc/apns-conf-us.xml:system/etc/apns-conf.xml``.At runtime, the Android reads APNs from the following file:``system/etc/apns-conf.xml``.

Android supports the following run-time network configuration methods to choose the appropriate APN from the list of configured APNs:
+ Automatic Configuration: At boot time, Android determines the correct network configuration based on the MCC and MNC from the SIM card and automatically configure all network settings.
+ Manual Configuration: The platform will also support runtime (user) manual selection of network settings by name, for example, "Company Name US," and will support manual network configuration entry.
+ WAP / SMS Push Configuration: The network configurations are standard Android resources. You can upgrade a resource at runtime by installing a new system resource APK package. It will be possible to develop a network configuration service which listens to a specific binary SMS port for binary SMS messages containing the network configurations. NOTE: The implementation will likely be network operator dependent due to inconsistent SMS ports, binary SMS formats, etc. 

+ Customizing pre-loaded applications
To customize the list of Android packages for a particular product (applications, input methods, providers, services, etc.), set ``PRODUCT_PACKAGES`` property in the product configuration, as illustrated below:

```bash
PRODUCT_PACKAGES := \
 <company_name>Mail \
 <company_name>IM \
 <company_name>HomeScreen \
 <company_name>Maps \
 <company_name>SystemUpdater
```

+ Customizing browser bookmarks
Browser bookmarks are stored as string resources in the Browser application: ``/android/packages/apps/Browser/res/values/strings.xml``. Bookmarks are defined as simple value string arrays called "bookmarks". Each bookmark entry is stored as a pair of array values; the first represents the bookmark name and the second the bookmark URL.

+ Email Provider Customization
``/android/packages/apps/Email/res/xml/providers.xml``

+ Themes and Styles
System level styles are defined in ``/android/framework/base/core/res/res/values/styles.xml``.

+ Animations
Android supports configurable animations for window and view transitions. System-level animations are defined in XML in global resource files located in ``/android/framework/base/core/res/res/anim/``.

### System ###
#### Bring up ####
+ Confirm a Clean Installation of a Basic Linux Kernel

+ Modify Your Kernel Configuration to Accommodate Android Drivers
```bash
#
# Android
#
# CONFIG_ANDROID_GADGET is not set
# CONFIG_ANDROID_RAM_CONSOLE is not set
CONFIG_ANDROID_POWER=y
CONFIG_ANDROID_POWER_STAT=y
CONFIG_ANDROID_LOGGER=y
# CONFIG_ANDROID_TIMED_GPIO is not set
CONFIG_ANDROID_BINDER_IPC=y
```

+ Write Drivers
[Audio](http://www.kandroid.org/online-pdk/guide/audio.html),[Keymaps&Keyboard](http://www.kandroid.org/online-pdk/guide/keymaps_keyboard_input.html),[Display](http://www.kandroid.org/online-pdk/guide/display_drivers.html)

+ Burn Images to Flash
An image represents the state of a system or part of a system stored in non-volatile memory. The build process should produce the following system images:
    - bootloader: The bootloader is a small program responsible for initiating loading of the operating system.
    - boot:
    - recovery:
    - system: The system image stores a snapshot of the Android operating system.
    - data: The data image stores user data. Anything not saved to the device/data directory will be lost on reboot.
    - kernel: The kernel represents the most basic element of an operating system. Android's Linux kernel is responsible for managing the system's resources and acts as an abstraction layer between hardware and a system's applications.
    - ramdisk: RAMdisk defines a portion of Random Access Memory (RAM) that gets used as if it were a hard drive.
Configure the bootloader to load the kernel and RAMdisk into RAM and pass the RAMdisk address to the kernel on startup.

+ Boot the kernel and mount the RAMdisk.

+ Debug Android-specific init programs on RAMdisk
Android-specific init programs are found in ``device/system/init``. Add LOG messages to help you debug potential problems with the LOG macro defined in ``device/system/init/init.c``.The ``init`` program directly mounts all filesystems and devices using either hard-coded file names or device names generated by probing the sysfs filesystem (thereby eliminating the need for a ``/etc/fstab`` file in Android). After ``device/system`` files are mounted, init reads ``/etc/init.rc`` and invokes the programs listed there (one of the first of which is the console shell).

+ Verify that applications have started
Once the shell becomes available, execute ``% ps`` to confirm that the following applications are running:
```bash
/system/bin/logd
/sbin/adbd
/system/bin/usbd
/system/bin/debuggerd
/system/bin/rild
/system/bin/app_process
/system/bin/runtime
/system/bin/dbus-daemon
system_server
```
Each of these applications is embedded Linux C/C++ and you can use any standard Linux debugging tool to troubleshoot applications that aren't running. Execute ``% make showcommands`` to determine precise build commands. ``gdbserver`` (the GNU debugger) is available in the bin directory of the system partition.

+ Pulling it all together
If bring up was successful, you should see the following Java applications (with icons) visible on the LCD panel:
```bash
com.google.android.phone: The Android contact application.
com.google.android.home
android.process.google.content
```
If they are not visible or unresponsive to keypad control, run the ``framebuffer/keypad`` tests.

[Android Init Language](http://www.kandroid.org/online-pdk/guide/bring_up.html)

#### Connectivity ####
[Bluetooth](http://www.kandroid.org/online-pdk/guide/bluetooth.html),[GPS](http://www.kandroid.org/online-pdk/guide/gps.html),[Wi-Fi](http://www.kandroid.org/online-pdk/guide/wifi.html)

#### Display Drivers ####
[Display Drivers](http://www.kandroid.org/online-pdk/guide/display_drivers.html)

#### Input Devices ####
[Keymaps&Keyboard](http://www.kandroid.org/online-pdk/guide/keymaps_keyboard_input.html)

#### Lights ####
[Lights](http://www.kandroid.org/online-pdk/guide/lights.html)

#### Multimedis ####
[Audio](http://www.kandroid.org/online-pdk/guide/audio.html),[Camera/Video](http://www.kandroid.org/online-pdk/guide/camera.html)

#### PowerManagement ####
Android supports its own Power Management (on top of the standard Linux Power Management) designed with the premise that the CPU shouldn't consume power if no applications or services require power. Android requires that applications and services request CPU resources with "wake locks" through the Android application framework and native Linux libraries. If there are no active wake locks, Android will shut down the CPU.

##### Wake Locks #####
Wake locks are used by applications and services to request CPU resources.A locked wakelock, depending on its type, prevents the system from entering suspend or other low-power states. This document describes how to employ wakelocks.There are two settings for a wakelock:
+ ``WAKE_LOCK_SUSPEND``: prevents a full system suspend.
+ ``WAKE_LOCK_IDLE``: low-power states, which often cause large interrupt latencies or that disable a set of interrupts, will not be entered from idle until the wakelocks are released.

Unless the type is specified, this document refers to wakelocks of type ``WAKE_LOCK_SUSPEND``.If the suspend operation has already started when locking a wakelock, the system will abort the suspend operation as long it has not already reached the ``suspend_late`` stage. This means that locking a wakelock from an interrupt handler or a freezeable thread always works, but if you lock a wakelock from a ``suspend_late`` handler, you must also return an error from that handler to abort suspend. You can use wakelocks to allow the user-space to decide which keys should wake the full system and turn on the screen. Use ``set_irq_wake`` or a platform-specific API to ensure that the keypad interrupt wakes up the CPU. Once the keypad driver has resumed, the sequence of events can look like this:

+ The Keypad driver receives an interrupt, locks the keypad-scan wakelock, and starts scanning the keypad matrix.
+ The keypad-scan code detects a key change and reports it to the input-event driver.
+ The input-event driver sees the key change, enqueues an event, and locks the input-event-queue wakelock.
+ The keypad-scan code detects that no keys are held and unlocks the keypad-scan wakelock.
+ The user-space input-event thread returns from ``select/poll``, locks the process-input-events wakelock, and calls read in the input-event device.
+ The input-event driver dequeues the key-event and, since the queue is now empty, unlocks the input-event-queue wakelock.
+ The user-space input-event thread returns from read. It determines that the key should not wake up the full system, releases the process-input-events wakelock, and calls ``select`` or ``poll``.

User-space API
Write lockname or lockname timeout to ``/sys/power/wake_lock`` lock and, if needed, create a wakelock. The timeout here is specified in nanoseconds. Write lockname to ``/sys/power/wake_unlock`` to unlock a user wakelock.

All power management calls follow the same basic format:
+ Acquire handle to the PowerManager service.
+ Create a wake lock and specify the power management flags for screen, timeout, etc.
+ Acquire wake lock.
+ Perform operation (play MP3, open HTML page, etc.).
+ Release wake lock.
```bash
PowerManager pm = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK| PowerManager.ON_AFTER_RELEASE,TAG);
wl.acquire();
 // ...
wl.release();
```

The Android Framework exposes power management to services and applications through the ``PowerManager`` class.User space native libraries (any hardware function in ``/device/lib/hardware/`` meant to serve as supporting libraries for Android runtime) should never call into Android Power Management directly (see the image above). Bypassing the power management policy in the Android runtime will destabilize the system.All calls into Power Management should go through the Android runtime ``PowerManager`` APIs.

#### Sensors ####
[Sensors](http://www.kandroid.org/online-pdk/guide/sensors.html)

#### Telephony ####
[Radio Interface Layer](http://www.kandroid.org/online-pdk/guide/telephony.html),[Sim Toolkit Application](http://www.kandroid.org/online-pdk/guide/stk.html)

### Dalvik ###
[Dalvik](http://www.kandroid.org/online-pdk/guide/dalvik.html)

### Testing and Debugging ###
[Instrumentation Testing](http://www.kandroid.org/online-pdk/guide/instrumentation_testing.html),[Debugging with GDB](http://www.kandroid.org/online-pdk/guide/debugging_gdb.html),[Debugging Native Code](http://www.kandroid.org/online-pdk/guide/debugging_native.html),[Debugging with tcpdump](http://www.kandroid.org/online-pdk/guide/tcpdump.html)
