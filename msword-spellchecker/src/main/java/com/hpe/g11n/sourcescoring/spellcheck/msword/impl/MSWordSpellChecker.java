package com.hpe.g11n.sourcescoring.spellcheck.msword.impl;

import com.hpe.g11n.sourcescoring.spellcheck.ISpellChecker;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-22
 * Time: 10:39
 *
 * can find some JACOB doc in <url>http://www.land-of-kain.de/docs/jacob/</url> <br>
 * MS office doc https://msdn.microsoft.com/en-us/library/office/ff822597.aspx,
 * https://msdn.microsoft.com/en-us/library/office/ff835170.aspx
 */
public class MSWordSpellChecker implements ISpellChecker {
    @Override
    public String suggestion(String source) {
        ActiveXComponent word = null;
        ActiveXComponent document = null;
        String savePath="C:\\Users\\Administrator\\Desktop\\style\\aaa.doc";
        try{
            StringBuffer sb=new StringBuffer(100);
            word = new ActiveXComponent("Word.Application");
            //we can use word.invoke("CheckSpelling",source); to check whether spell error.
            //word.invoke("GetSpellingSuggestions",source) to get spellingSuggestions..
            //document CheckSpelling will display checkspelling dialog...
            //use content CheckSpelling to get suggestion.
            word.setProperty("Visible", Variant.VT_FALSE);
            ActiveXComponent docs=word.getPropertyAsComponent("Documents");
            document= docs.invokeGetComponent("Add");
            ActiveXComponent content= document.getPropertyAsComponent("content");
            content.setProperty("text", "This word is for spelling check only!!");
            if(!word.invoke("CheckSpelling",source).getBoolean()){
                Variant result  = word.invoke("GetSpellingSuggestions",source);
                int cnt = Dispatch.get(result.getDispatch(),"count").getInt();

                for(int i=1;i<=cnt;i++){
                    Variant item=Dispatch.call(result.getDispatch(), "Item", i);
                    sb.append(Dispatch.get(item.getDispatch(),"name").getString()).append(",");
                }
                return sb.toString();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            word.invoke("Quit", 0);
        }
        return null;
    }
}
