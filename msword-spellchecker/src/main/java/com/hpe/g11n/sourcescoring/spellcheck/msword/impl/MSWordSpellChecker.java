package com.hpe.g11n.sourcescoring.spellcheck.msword.impl;

import com.hpe.g11n.sourcescoring.spellcheck.ISpellChecker;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Variant;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-22
 * Time: 10:39
 *
 * can find some JACOB doc in <url>http://www.land-of-kain.de/docs/jacob/</url> <br>
 * MS office doc https://msdn.microsoft.com/en-us/library/office/ff822597.aspx
 */
public class MSWordSpellChecker implements ISpellChecker {
    @Override
    public String suggestion(String source) {
        ActiveXComponent word = null;
        ActiveXComponent document = null;
        String savePath="C:\\Users\\Administrator\\Desktop\\style\\aaa.doc";
        try{
            word = new ActiveXComponent("Word.Application");
            //we can use word.invoke("CheckSpelling",source); to check whether spell error.
            //use content or document CheckSpelling to get suggestion.
            word.setProperty("Visible", Variant.VT_TRUE);
            ActiveXComponent docs=word.getPropertyAsComponent("Documents");
            document= docs.invokeGetComponent("Add");
            ActiveXComponent content= document.getPropertyAsComponent("content");
            content.setProperty("text", source);
            Variant result  = content.invoke("CheckSpelling");
            return result.getString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            word.getPropertyAsComponent("WordBasic").invoke("FileSaveAs", savePath);
            document.invoke("Close", false);
            word.invoke("Quit", 0);
        }
        return null;
    }
}
