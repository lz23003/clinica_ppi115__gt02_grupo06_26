package sv.edu.ues_ingenieria_pp115_2026_salud.odontologia_sv.boundary.jsf;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter<Object> {

    private static final String VIEW_MAP_KEY = "entityConverter.objects";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return this.getObjectsFromView(context).get(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        
        String id = null;
        Map<String, Object> map = this.getObjectsFromView(context);
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                id = entry.getKey();
                break;
            }
        }
        
        if (id == null) {
            id = UUID.randomUUID().toString();
            map.put(id, value);
        }
        
        return id; 
    }

    private Map<String, Object> getObjectsFromView(FacesContext context) {
        Map<String, Object> viewMap = context.getViewRoot().getViewMap();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) viewMap.get(VIEW_MAP_KEY);
        if (map == null) {
            map = new HashMap<>();
            viewMap.put(VIEW_MAP_KEY, map);
        }
        return map;
    }
}