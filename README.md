# NFC_Library
EzNFC is library for simplify work with NFC tags in android. With EzNFC you can work with basic operations for NFC, 
which are reading from NFC tag, writting text and writting URL. 
##  In your setting.gradle add:

     maven { url 'https://jitpack.io' }

### example:
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
            maven { url 'https://jitpack.io' }
        }
    }

## add implementation of EzNFC into your build.gradle dependencies.
    implementation 'com.github.vojmahdal:Ez_NFC:1.0.1'

## Create techlist.xml and add this code:
    <resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
    <tech-list>
        <tech>android.nfc.tech.NfcA</tech>
        <tech>android.nfc.tech.Ndef</tech>
    </tech-list>
    <tech-list>
        <tech>android.nfc.tech.NfcA</tech>
    </tech-list>
    <tech-list>
        <tech>android.nfc.tech.Ndef</tech>
    </tech-list>
    <tech-list>
        <tech>android.nfc.tech.NfcA</tech>
        <tech>android.nfc.tech.Ndef</tech>
        <tech>android.nfc.tech.MifareUltralight</tech>
    </tech-list>
    </resources>

## In your AndroidManifest.xml add:

    <uses-permission android:name="android.permission.NFC"/>
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-feature android:name="android.hardware.nfc" android:required="true"/>
 ## And in activity, which use NFC, add:
    <intent-filter>
                <action android:name="android.nfc.action.NDEF_DISCOVERED" />
                <action android:name="android.nfc.action.TAG_DISCOVERED" />
                <action android:name="android.nfc.action.TECH_DISCOVERED" />

                <data android:mimeType="text/plain"/>
            </intent-filter>

            <meta-data
                android:name="android.nfc.action.TECH_DISCOVERED"
                android:resource="@xml/techlist" />


## In your Activity class:
          private var intentFilterArray: Array<IntentFilter>? = null
          private var pendingIntent: PendingIntent? = null
          private val nfcAdapter: NfcAdapter? by lazy {
                  NfcAdapter.getDefaultAdapter(this)
              }
              private var nfcLib = EzNfc(this, intentFilterArray)


## in fun onCreate set nfc library nfcAdapter, pendingIntent and intentFilterArray:
         nfcLib.nfcAdapter = nfcAdapter
         pendingIntent = PendingIntent.getActivity(
                    this, 
                    0,
                    Intent(this, javaClass)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    0)
         intentFilterArray = nfcLib.onCreateFilter()
  
 ## in fun onResume use EzNFC onResume with parameter of your pending Intent:
        nfcLib.onResume(pendingIntent)
   
## in fun onPause() use EzNFC onPause:
        nfcLib.onPause()
  
## in fun onNewIntent(intent: Intent) add operation of NFC: read, writeText or writeUrl (example of write):
      nfcLib.writeText(intent, "text")
  
