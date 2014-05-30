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
        InputMethodManager imm = (InputMethodManager) this
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