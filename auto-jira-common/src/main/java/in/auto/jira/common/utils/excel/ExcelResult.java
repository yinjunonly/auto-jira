package in.auto.jira.common.utils.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * @discription 获得读取excel的结果
 * @author lichao
 *
 * @param <T>
 */
public class ExcelResult<T> {

    private List<T>      values = new ArrayList<T> ();

    private List<String> errs   = new ArrayList<String> ();

    public ExcelResult() {
        super ();
    }

    public ExcelResult(List<T> values, List<String> errs) {
        super ();
        this.values = values;
        this.errs = errs;
    }

    // 是否有load错误
    public boolean hasError(){
        if (errs != null && errs.size () > 0) { return true; }
        return false;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer ();
        if (errs != null) {
            for ( String str : errs ) {
                sb.append (str + " ;");
            }
        }
        return sb.toString ();
    }

    public List<T> getValues(){
        if (this.hasError ()) { throw new RuntimeException ("加载的Excel中解析有错误，请解决错误时候再保存"); }
        return values;
    }

    public void setValues(List<T> values){
        this.values = values;
    }

    public List<String> getErrs(){
        return errs;
    }

    public void setErrs(List<String> errs){
        this.errs = errs;
    }

}
