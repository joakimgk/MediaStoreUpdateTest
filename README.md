# MediaStoreUpdateTest
Android test app to reproduce failure to update MediaStore DESCRIPTION on Android 10

## Steps to reproduce:
* create a virtual device
* drag one or more JPEG files onto it
* open Photos app, to make sure the MediaStore is updated
* run app twice (first time it fails due to missing WRITE permission)
* on the second run, the FIRST (newest) image on the device should be successfully updated on API < 29, while API 29 logs a warning
