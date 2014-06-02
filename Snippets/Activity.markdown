Snippets related to Activity
=============================
+ ``AlertDialog``
```java
protected AlertDialog showAlertDialog(String pTitle, String pMessage,
        DialogInterface.OnClickListener pOkClickListener,
        DialogInterface.OnClickListener pCancelClickListener,
        DialogInterface.OnDismissListener pDismissListener) {
    mAlertDialog = new AlertDialog.Builder(this)
            .setTitle(pTitle)
            .setMessage(pMessage)
            .setPositiveButton(android.R.string.ok, pOkClickListener)
            .setNegativeButton(android.R.string.cancel,
                    pCancelClickListener).show();
    if (pDismissListener != null) {
        mAlertDialog.setOnDismissListener(pDismissListener);
    }
    return mAlertDialog;
}
```

```java
protected AlertDialog showAlertDialog(String pTitle, String pMessage,
        String pPositiveButtonLabel, String pNegativeButtonLabel,
        DialogInterface.OnClickListener pOkClickListener,
        DialogInterface.OnClickListener pCancelClickListener,
        DialogInterface.OnDismissListener pDismissListener) {
    mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle)
            .setMessage(pMessage)
            .setPositiveButton(pPositiveButtonLabel, pOkClickListener)
            .setNegativeButton(pNegativeButtonLabel, pCancelClickListener)
            .show();
    if (pDismissListener != null) {
        mAlertDialog.setOnDismissListener(pDismissListener);
    }
    return mAlertDialog;
}
```

```java
protected void hideKeyboard(View view) {
    InputMethodManager imm = (InputMethodManager) view.getContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
}
```

```java
public void setAlertDialogIsClose(DialogInterface pDialog, Boolean pIsClose) {
    try {
        Field field = pDialog.getClass().getSuperclass()
                .getDeclaredField("mShowing");
        field.setAccessible(true);
        field.set(pDialog, pIsClose);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

```java
protected void openActivity(String pAction, Bundle pBundle) {
    Intent intent = new Intent(pAction);
    if (pBundle != null) {
        intent.putExtras(pBundle);
    }
    startActivity(intent);
}
```

```java
public static void hideSoftInput(View view) {
    if (view == null)
        return;
    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm.isActive()) {
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}
```

```java
public static void showSoftInput(View view) {
    if (view == null)
        return;
    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(view, 0);
}
```

```java
private static int cleanFile(File dir, long maxInterval)
{
    File[] files = dir.listFiles();
    if(files == null) return 0;
    int beforeNum = 0;
    long current = System.currentTimeMillis();
    for(File file:files)
    {
        long lastModifiedTime = file.lastModified();
        if((current-lastModifiedTime) > maxInterval)
        {
            //if the file is exist more than a week , so need to delete.
            file.delete();
            beforeNum ++;
        }
    }
    return beforeNum;
}
```