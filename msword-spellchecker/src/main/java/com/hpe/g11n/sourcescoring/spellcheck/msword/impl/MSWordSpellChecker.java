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
 * can find some JACOB doc in
 * http://www.land-of-kain.de/docs/jacob <br>
 *
 * MS office doc
 * https://msdn.microsoft.com/en-us/library/office/ff822597.aspx<br>
 * https://msdn.microsoft.com/en-us/library/office/ff835170.aspx<br>
 * https://msdn.microsoft.com/en-us/library/office/ff821618.aspx<br>
 * https://msdn.microsoft.com/en-us/library/office/ff839188.aspx<br>
 */
public class MSWordSpellChecker implements ISpellChecker {
    @Override
    public String suggestion(String source) {
        ActiveXComponent msWord = null;
        ActiveXComponent document = null;
        try{
            StringBuffer sb=new StringBuffer(100);
            msWord = new ActiveXComponent("Word.Application");
            msWord.setProperty("Visible", Variant.VT_FALSE);
            ActiveXComponent docs=msWord.getPropertyAsComponent("Documents");
            document= docs.invokeGetComponent("Add");
            ActiveXComponent content = document.getPropertyAsComponent("content");
            content.setProperty("text", "This word is for spelling check only!!");
            if(!msWord.invoke("CheckSpelling",source).getBoolean()){
                Variant result  = msWord.invoke("GetSpellingSuggestions", source);
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
            msWord.invoke("Quit", 0);
        }
        return null;
    }
}
