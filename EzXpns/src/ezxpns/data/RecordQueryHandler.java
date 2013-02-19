package ezxpns.data;

import java.util.*;

public interface RecordQueryHandler<T> {
	Vector<T> getRecordsBy(String name, int max);
	Vector<T> getRecordsBy(Category category, int max);
	Vector<T> getRecordsBy(Date start, Date end, int max, boolean reverse);
}
