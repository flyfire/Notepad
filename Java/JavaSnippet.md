JavaSnippet
==============

+ Unicode2UTF-8 [REF](http://javaprogramming.language-tutorial.com/2012/09/unicode-to-utf-8-conversion-program.html)
```java
import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Unicode2UTFConverter {
 /**
  * Creates a new UTF8-encoded byte array representing the
  * char[] passed in. The output array will NOT be null-terminated.
  *
  *
  * @param unicode An array of Unicode characters, which may have UCS4
  * characters encoded in UTF-16.  This array must not be null.
  * @exception CharConversionException If the input characters are invalid.
  */
 protected static byte[] UnicodeToUTF8(char[] unicode, boolean nullTerminate)
  throws CharConversionException
 {
  int uni; // unicode index
  int utf; // UTF8 index
  int maxsize; // maximum size of UTF8 output
  byte[] utf8 = null; // UTF8 output buffer
  byte[] temp = null; // used to create an array of the correct size
  char ch; // Unicode character
  int ucs; // UCS4 encoding of a character
  boolean failed = true;
  
 
  if(unicode == null) { return null;}

  try {

   // Allocate worst-case size (UTF8 bytes == 1.5 times Unicode bytes)
   maxsize = unicode.length * 3; //chars are 2 bytes each
   if(nullTerminate) {
    maxsize++;
   }
   utf8 = new byte[maxsize];
   
   for(uni=0, utf=0; uni < unicode.length; uni++) {
    
//    Convert UCS2 to UCS4  
//     Assuming that character may have UTF-16 encoding
    ch = unicode[uni];
    if( ch >= 0xd800 && ch <= 0xdbff) {
//     This is the high half of a UTF-16 char
     ucs = (ch-0xd800)<<10;

//     Now get the lower half
     if(uni == unicode.length-1) { throw new CharConversionException();} //There is no lower half
     
     ch = unicode[++uni];
     if(ch < 0xdc00 || ch > 0xdfff) {throw new CharConversionException();} // not in the low-half zone
     
     ucs |= ch-0xdc00;
     ucs += 0x00010000;
     
    } else if(ch >=0xdc00 && ch <=0xdfff) {throw new CharConversionException(); // orphaned low-half char 
    
    } else { ucs = unicode[uni]; // UCS2 char to UCS4
    }


    
//     UCS4 to UTF8 conversion
//    Note that the Standard UTF encoding is allowed till 4 bytes i.e < 10FFFF. However this program can encode till 6 bytes of unicode character
    
    if(ucs < 0x80) {
     // 0000 0000 - 0000 007f (ASCII)
     utf8[utf++] = (byte)ucs;
    } else if(ucs < 0x800) {
     // 0000 0080 - 0000 07ff
     utf8[utf++] = (byte) (0xc0 | ucs>>6);
     utf8[utf++] = (byte) (0x80 | (ucs & 0x3f) );
    } else if(ucs < 0x0010000) {
     // 0000 0800 - 0000 ffff
     utf8[utf++] = (byte) (0xe0 | ucs>>12);
     utf8[utf++] = (byte) (0x80 | ((ucs>>6) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | (ucs & 0x3f) );
    } else if(ucs < 0x00200000) {
     // 001 0000 - 001f ffff
     utf8[utf++] = (byte) (0xf0 | ucs>>18);
     utf8[utf++] = (byte) (0x80 | ((ucs>>12) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | ((ucs>>6) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | (ucs & 0x3f) );
    } else if(ucs < 0x00200000) {
     // 0020 0000 - 03ff ffff
     utf8[utf++] = (byte) (0xf8 | ucs>>24);
     utf8[utf++] = (byte) (0x80 | ((ucs>>18) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | ((ucs>>12) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | ((ucs>>6) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | (ucs & 0x3f) );
     System.out.println(currentDate() + " :Warning: UTF-8 code for Unicode Character is Illegal");
    } else {
     // 0400 0000 - 7fff ffff
     utf8[utf++] = (byte) (0xfc | ucs>>30);
     utf8[utf++] = (byte) (0x80 | ((ucs>>24) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | ((ucs>>18) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | ((ucs>>12) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | ((ucs>>6) & 0x3f) );
     utf8[utf++] = (byte) (0x80 | (ucs & 0x3f) );
     System.out.println(currentDate() + " :Warning: UTF-8 code for Unicode Character is Illegal");
    }
   
   }
   
   
   if(nullTerminate) { utf8[utf++] = (byte)0x0a; }// CR+LF
  
   // Copy into a correct-sized array
   
   try {
    int i;
    // last index is the size of the UTF8
    temp = new byte[utf];
    for(i=0; i < utf; i++) {
     temp[i] = utf8[i];
     utf8[i] = 0;
    }
    utf8 = temp;
    temp = null;
   } finally {
   }

   failed = false;
   return utf8;

  } finally {
   // Cleanup data locations where the password was written
   if(failed && utf8 != null)  {utf8 = null;}
  
   ucs = 0;
   ch = 0;
    }
 }
 /**
  * Main method
  */
 public static void main(String[] args) {

  char[] unicode;
  byte[] utf8;

  if (args.length !=2)
  {
   System.out.println("Usage: java UnicodeToUTF8 <input unicode filename> <output utf8 filename>");
   System.exit(0);
  }
  String InFilePath = args[0]; //Input filename is first argument.
  String OutFilePath = args[1]; //Output filename is Second argument.
  System.out.println(currentDate() +" : Starting Unicode to UTF8 Conversion");
  
  try{
      BufferedReader lin= new BufferedReader(new InputStreamReader
        (new FileInputStream(InFilePath)));
        
      FileOutputStream fos = new FileOutputStream(OutFilePath);
 
      String ls = new String(); // A temp val to hold each line.
    
       while((ls = lin.readLine()) != null)
       {
         unicode = ls.toCharArray();
        utf8 = UnicodeToUTF8(unicode,true);
        
         fos.write(utf8);
       }
       lin.close();
       fos.close();
       System.out.println(currentDate() +" : Unicode to UTF8 Conversion Successful");
       
   } catch(CharConversionException e) {
    System.out.println("Error converting Unicode "+e);
   }
      catch(Exception e){e.printStackTrace();}
  }
 
 private static String currentDate(){
  DateFormat shortFormatter = SimpleDateFormat.getDateTimeInstance( SimpleDateFormat.SHORT,
                SimpleDateFormat.MEDIUM  );

  long currentTimeInMillis = System.currentTimeMillis();
     Date today = new Date( currentTimeInMillis);
     return shortFormatter.format( today ).toString();
 }
}
```

```java
package test.wingware;

public class UnicodeToUTF8 {

	private static final int MASKBITS = 0x3F;
	private static final int MASKBYTE = 0x80;
	private static final int MASK2BYTES = 0xC0;
	private static final int MASK3BYTES = 0xE0;

	// private static final int MASK4BYTES = 0xF0;
	// private static final int MASK5BYTES = 0xF8;
	// private static final int MASK6BYTES = 0xFC;
	/** */
	/**
	 * @功能: 将UNICODE（UTF-16LE）编码转成UTF-8编码
	 * @参数: byte[] b 源字节数组
	 * @返回值: byte[] b 转为UTF-8编码后的数组
	 * @作者: imuse
	 */
	public static byte[] UNICODE_TO_UTF8(byte[] b) {
		int i = 0;
		int j = 0;
		byte[] utf8Byte = new byte[b.length * 2];
		while (i < b.length) {
			byte[] bUTF = new byte[1];
			int nCode = (b[i] & 0xFF) | ((b[i + 1] & 0xFF) << 8);
			if (nCode < 0x80) {
				bUTF = new byte[1];
				bUTF[0] = (byte) nCode;
			}
			// 110xxxxx 10xxxxxx
			else if (nCode < 0x800) {
				bUTF = new byte[2];
				bUTF[0] = (byte) (MASK2BYTES | nCode >> 6);
				bUTF[1] = (byte) (MASKBYTE | nCode & MASKBITS);
			}
			// 1110xxxx 10xxxxxx 10xxxxxx
			else if (nCode < 0x10000) {
				bUTF = new byte[3];
				bUTF[0] = (byte) (MASK3BYTES | nCode >> 12);
				bUTF[1] = (byte) (MASKBYTE | nCode >> 6 & MASKBITS);
				bUTF[2] = (byte) (MASKBYTE | nCode & MASKBITS);
			}
			for (int k = 0; k < bUTF.length; k++) {
				utf8Byte[j++] = bUTF[k];
			}
			i += 2;
		}
		b = new byte[j];
		System.arraycopy(utf8Byte, 0, b, 0, j);
		return b;
	}

	/** */
	/**
	 * @功能: 将一个长度为2 byte数组转为short
	 * @参数: byte[] bytesShort要转的字节数组
	 * @返回值: short sRet 转后的short值
	 */
	public static short bytesToShort(byte[] bytesShort) {
		short sRet = 0;
		sRet += (bytesShort[0] & 0xFF) << 8;
		sRet += bytesShort[1] & 0xFF;
		return sRet;
	}

	/** */
	/**
	 * @功能: 将一个short值转为byte数组
	 * @参数: short sNum 要转的short值
	 * @返回值: byte[] bytesRet 转后的byte数组
	 */
	public static byte[] shortToBytes(short sNum) {
		byte[] bytesRet = new byte[2];
		bytesRet[0] = (byte) ((sNum >> 8) & 0xFF);
		bytesRet[1] = (byte) (sNum & 0xFF);
		return bytesRet;
	}

	/** */
	/**
	 * @功能: 将一个长度为4 byte数组转为int
	 * @参数: byte[] bNum要转的字节数组
	 * @返回值: int retInt 转后的int值
	 */
	public static int bytesToInt(byte[] bNum) {
		int retInt = 0;
		retInt = ((bNum[0] & 0xFF) << 24);
		retInt += (bNum[1] & 0xFF) << 16;
		retInt += (bNum[2] & 0xFF) << 8;
		retInt += bNum[3] & 0xFF;
		return retInt;
	}

	/** */
	/**
	 * @功能: 将一个int值转为byte数组
	 * @参数: int nNum 要转的int值
	 * @返回值: byte[] bytesRet 转后的byte数组
	 */
	public static byte[] intToBytes(int nNum) {
		byte[] bytesRet = new byte[4];
		bytesRet[0] = (byte) ((nNum >> 24) & 0xFF);
		bytesRet[1] = (byte) ((nNum >> 16) & 0xFF);
		bytesRet[2] = (byte) ((nNum >> 8) & 0xFF);
		bytesRet[3] = (byte) (nNum & 0xFF);
		return bytesRet;
	}

}
```