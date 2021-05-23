package model;
import java.io.Serializable;
import java.util.List;

public class Chart implements Serializable {
    private static final long serialVersionUID = 1L;
        private  String name;
        private List<Week> week;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public List<Week> getWeek() {
            return week;
        }
        public void setWeek(List<Week> week) {
            this.week = week;
        }

}
