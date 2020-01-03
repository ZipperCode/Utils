package export;

import export.base.DataFieldName;
import export.base.DataName;

import java.util.Date;

@DataName()
public class ExportBeanChild extends ExportBean{
    @DataFieldName
    private Date date;

    public ExportBeanChild(String eid, String name, int age) {
        super(eid, name, age);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
