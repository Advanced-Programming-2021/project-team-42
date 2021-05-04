    package Controller;

    import java.util.*;

    public class ScoreBoardController {
        public Map<String, Integer> scores = new HashMap<String, Integer>();
        void sortByValue(boolean order) {
//convert HashMap into List
            List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(scores.entrySet());
//sorting the list elements
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    if (order) {
//compare two object and return an integer
                        return o1.getValue().compareTo(o2.getValue());
                    } else {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                }
            });
//prints the sorted HashMap
            Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
            for (Map.Entry<String, Integer> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            printMap(sortedMap);
        }
        public void printMap(Map<String, Integer> map) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                System.out.println("<" + entry.getKey() + ">" + ":" + "<" + entry.getValue() + ">");
            }
            System.out.println("\n");
        }
    }

