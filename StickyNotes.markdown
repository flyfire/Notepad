+ Activity ``getIntent()`` Return the intent that started this activity.
+ PackageManager ``queryIntentActivities(Intent intent, int flags)`` Retrieve all activities that can be performed for the given intent. 取得所有可以对给定intent响应的activity。
+ ResolveInfo ``loadLabel(PackageManager pm)`` Retrieve the current textual label associated with this resolution.
+ ``ApplicationInfo`` Information you can retrieve about a particular application. This corresponds to information collected from the ``AndroidManifest.xml's <application>`` tag.