/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capas;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author valde
 */
public class Database {
    private int primaryKey;
    private Map<String, Object[]> rows;
    
    public Database() {
        this.primaryKey = 0;
        this.rows = new HashMap<>();
    }

    private boolean isBlankId(Object id) {
        if (id == null) return true;
        String s = id.toString();
        return s == null || s.trim().isEmpty();
    }

    public Object [] store (Object[] data ){
     if (data == null || data.length == 0) return null;
     if (isBlankId(data[0])){
         this.primaryKey++;
         data[0] = this.primaryKey;
     }
     rows.put (data[0].toString(), data);
     return data;
    }
    public void delete (String id){
        this.rows.remove(id);
    }
    
    public Object[] find (String id){
    return this.rows.get(id);
    }
    
    public Map<String, Object[]> list (){
        return this.rows;
    }
    public Object[] insert(Object[] data) {
        if (data == null || data.length == 0) return null;
        this.primaryKey++;
        data[0] = this.primaryKey;
        this.rows.put(data[0].toString(), data);
        return data;
    }
   
     public Object[] update (Object[] data) {
        if (data == null || data.length == 0) return null;
        this.rows.put(data[0].toString(), data);
        return data;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Database{");
        sb.append("primaryKey=").append(primaryKey);
        sb.append(", records=").append(this.rows.size());
        sb.append(", data=").append(this.rows);
        sb.append('}');
        return sb.toString();
    }
}