package export;

import export.base.DataFieldName;
import export.base.DataName;

@DataName(value = "Export")
public class ExportBean {
    @DataFieldName("ID")
    private String eid;
    @DataFieldName("名字")
    private String name;

    private int age;

    public ExportBean(String eid, String name, int age) {
        this.eid = eid;
        this.name = name;
        this.age = age;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
