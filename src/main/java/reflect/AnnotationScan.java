package reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫描包
 */
public class AnnotationScan {
    private List<String> packageNames;

    public AnnotationScan(String...args){
        packageNames = new ArrayList<>();
        if(args.length > 1){

        }
    }

    public void initScan(){

    }

}
