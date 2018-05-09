package com.wiwikeyandroid.bean;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.seny.android.utils.DateUtils;

import com.wiwikeyandroid.modules.Contacts.model.Person;

public class PinnedSectionBean {
	// 类型--内容
	public static final int ITEM = 0;
	// 类型--顶部的标题
	public static final int SECTION = 1;

	public final int type; // 所属于的类型
	public final Person detail; // listview显示的item的数据实体类

	public int sectionPosition; // 顶部标题的位置
	public int listPosition; // 内容的位置

	public int getSectionPosition() {
		return sectionPosition;
	}

	public void setSectionPosition(int sectionPosition) {
		this.sectionPosition = sectionPosition;
	}

	public Person getDetail() {
		return detail;
	}

	public int getListPosition() {
		return listPosition;
	}

	public void setListPosition(int listPosition) {
		this.listPosition = listPosition;
	}

	public PinnedSectionBean(int type, Person detail) {
		super();
		this.type = type;
		this.detail = detail;
	}

	public PinnedSectionBean(int type, Person detail, int sectionPosition,
			int listPosition) {
		super();
		this.type = type;
		this.detail = detail;
		this.sectionPosition = sectionPosition;
		this.listPosition = listPosition;
	}

	@Override
	public String toString() {
		return DateUtils.formatDateAndTimess(detail.getDate());
	}

	/**
	 * 将ArrayList的数据进行分组，返回带有分组Header的ArrayList。
	 * 
	 * @param details
	 *            从数据库接受到的ArrayList的数据，其中日期格式为：yyyy-MM-dd HH:mm:ss
	 * @return list 返回的list是分类后的包含header（yyyy-MM-dd）和item（HH:mm:ss）的ArrayList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<PinnedSectionBean> getData(List<Person> details) {
		// 最后我们要返回带有分组的list,初始化
		ArrayList<PinnedSectionBean> list = new ArrayList<PinnedSectionBean>();
		// Person作为key是yyyy-MM-dd格式,List<Person>是对应的值是HH:mm:ss格式
		Map<Person, List<Person>> map = new TreeMap<Person, List<Person>>();
		// 按照Person里面的时间进行分类
		Person detail = new Person();
		for (int i = 0; i < details.size(); i++) {
			try {
				String key = DateUtils.exchangeStringDate(DateUtils
						.formatDateAndTimess(details.get(i).getDate()));
				if (DateUtils.formatDate(detail.getDate()) != null
						&& !"".equals(DateUtils.formatDate(detail.getDate()))) {
					// 判断这个Key对象有没有生成,保证是唯一对象.如果第一次没有生成,那么new一个对象,之后同组的其他item都指向这个key
					boolean b = !key.equals(DateUtils.formatDate(
							detail.getDate()).toString());
					if (b) {
						detail = new Person();
					}
				}
				detail.setDate(DateUtils.formatToLong(key, "yyyy-MM-dd"));
				// 把属于当天yyyy-MM-dd的时间HH:mm:ss全部指向这个key
				List<Person> Persons = map.get(detail);
				// 判断这个key对应的值有没有初始化,若第一次进来,这new一个arryalist对象,之后属于这一天的item都加到这个集合里面
				if (Persons == null) {
					Persons = new ArrayList<Person>();
				}
				String time = DateUtils.formatDateAndTimess(details.get(i)
						.getDate());
				time = DateUtils.exchangeStringTime(time);
				// 把HH:mm:ss时间替换之前yyyy-MM-dd HH:mm:ss格式的时间.标识属于标题下的子类
				details.get(i)
						.setDate(DateUtils.formatToLong(time, "HH:mm:ss"));
				Persons.add(details.get(i));

				map.put(detail, Persons);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		// 用迭代器遍历map添加到list里面
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Entry) iterator.next();
			Person key = (Person) entry.getKey();
			// 我们的key(yyyy-MM-dd)作为标题.类别属于SECTION
			list.add(new PinnedSectionBean(SECTION, key));
			List<Person> li = (List<Person>) entry.getValue();
			for (Person Person : li) {
				// 对应的值(HH:mm:ss)作为标题下的item,类别属于ITEM
				list.add(new PinnedSectionBean(ITEM, Person));
			}
		}
		// 把分好类的hashmap添加到list里面便于显示
		return list;
	}
/*
	@Override
	public int compareTo(PinnedSectionBean another) {
        if(another!=null&&another.type==PinnedSectionBean.SECTION){
            if(this.getDetail().getDate()>another.getDetail().getDate()){

               return -1;
            }else if(this.getDetail().getDate()==another.getDetail().getDate()){

               return 0;

            }

       }
        return 1;
    }*/
}
