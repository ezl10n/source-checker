package com.hpe.g11n.sourcescoring.gui.utils;


import com.hp.g11n.sdl.psl.interop.core.IPassoloApp;
import com.hp.g11n.sdl.psl.interop.core.IPslProject;
import com.hp.g11n.sdl.psl.interop.core.IPslSourceLists;
import com.hp.g11n.sdl.psl.interop.core.impl.impl.PassoloApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassoloTemplate {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String source;
    public static PassoloTemplate build(String source){
        return new PassoloTemplate(source);
    }

    private PassoloTemplate(String source){
        this.source=source;
    }

    public void process(PassoloExecutor e){
        long start=System.currentTimeMillis();
        IPassoloApp app = PassoloApp.getInstance();
        IPslProject project = app.open(source);
        try{
            IPslSourceLists sourceLists= project.getSourceLists();
            e.execute(project, sourceLists);

        }catch (Exception ex){
            log.error("PassoloTempalte exception." + ex);
        }finally {
            project.close();
            IPassoloApp.quit();
            long end=System.currentTimeMillis();
            log.info("PassoloTemplate.process execute time:"+(end-start)+"ms");
        }
    }
}




