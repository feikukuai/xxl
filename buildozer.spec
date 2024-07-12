[app]

# (str) Title of your application
title = Hello App

# (str) Package name
package.name = helloapp

# (str) Package domain (needed for android/ios packaging)
package.domain = org.test

# (str) Source directory
source.dir = .

# (list) Source files to include (relative to source.dir)
source.include_exts = py,png,jpg,kv,atlas

# (list) List of inclusions using pattern matching
#source.include_patterns = assets/*,images/*.png

# (list) Source files to exclude (relative to source.dir)
#source.exclude_exts = spec

# (list) List of directory to exclude (relative to source.dir)
#source.exclude_dirs = tests, bin

# (list) List of exclusions using pattern matching
#source.exclude_patterns = license,images/*/*.jpg

# (str) Application version
version = 0.1

# (str) Application license
license = MIT

# Android specific
[android]

# (int) Android API to use
android.api = 28
android.skip_update = False
# (int) Android minimum API
android.minapi = 21

# (int) Android target API, should be as high as possible.
android.targetapi = 28

# (int) Android SDK version to use
android.sdk = 24

# (str) Android NDK version to use
android.ndk = 19b

# (int) Android NDK API to use. This is the minimum API level for the NDK platform.
android.ndk_api = 21
