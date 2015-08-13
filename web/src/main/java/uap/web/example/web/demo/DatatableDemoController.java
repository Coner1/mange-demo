package uap.web.example.web.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import uap.iweb.entity.DataTable;
import uap.iweb.entity.Field;
import uap.iweb.entity.Row;
import uap.iweb.icontext.IWebViewContext;
import uap.web.example.entity.DemoB;
import uap.web.example.service.demo.DemoBService;

@Component("iweb.DemoController")
public class DatatableDemoController {
	DataTable<DemoB> dataTable1;

	@Autowired
	private DemoBService demoService;

	public void loadData() throws Exception {

		int pageNumber = 0;
		Map<String, Object> parameters = IWebViewContext.getEventParameter();
		if (parameters.get("pageIndex") != null) {
			pageNumber = (Integer) parameters.get("pageIndex");
		}
		int pageSize = dataTable1.getPageSize();

		Map<String, Object> searchParams = new HashMap<String, Object>();

		searchParams = this.createSearchParamsStartingWith("search_");
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, "auto");
		Page<DemoB> categoryPage = demoService.getDemoPage(searchParams, pageRequest);
		dataTable1.remove(dataTable1.getAllRow());
		dataTable1.add(categoryPage.getContent().toArray(new DemoB[0]));

		int totalPages = categoryPage.getTotalPages();
		dataTable1.setTotalPages(totalPages);
		dataTable1.setPageIndex(pageNumber);
		dataTable1.setSelect(null);
	}

	private Map<String, Object> createSearchParamsStartingWith(String prefix) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> m = dataTable1.getParams();
		Set<Entry<String, Object>> set = m.entrySet();
		for (Entry<String, Object> entry : set) {
			String key = entry.getKey();
			if (key.startsWith(prefix)) {
				String unprefixed = key.substring(prefix.length());
				params.put(unprefixed, entry.getValue().toString());
			}
		}
		return params;
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "theid");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}
		return new PageRequest(pageNumber, pagzSize, sort);
	}

	public void update() {
		Row[] rowls = dataTable1.getAllRow();
		for (Row r : rowls) {
			if (this.isNeedSave(r)) {
				DemoB demoB = dataTable1.toBean(r);
				DemoB en;
				try {
					en = demoService.saveEntity(demoB);
					Long theid = en.getTheid();
					Field pkOrg = r.getField("theid");
					pkOrg.setValue(String.valueOf(theid));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		dataTable1.setSelect(null);

//		dataTable1.setCurrent(null);

	}

	public void del() {
		Row[] rows = dataTable1.getSelectRow();
		for (Row r : rows) {
			DemoB demoB = dataTable1.toBean(r);
			Long theid = demoB.getTheid();
			if (theid != null && theid != 0) {
				demoService.deleteById(theid);
			}
		}
		dataTable1.remove(rows);
		dataTable1.setSelect(null);
	}

	/**
	 * 判断传入进来的 行，是否需要保存，新增的行 和 改变的行 都需要保存
	 * 
	 * @param r
	 * @return
	 */
	private boolean isNeedSave(Row r) {
		String status = r.getStatus();
		if (status.endsWith(Row.STATUS_NEW)) {// 新增加的需要保存
			return true;
		}
		Map<String, Field> changefiled = r.getChangedFields();
		if (changefiled != null && changefiled.size() > 0) {
			return true; // 有字段发生变化，页需要保存
		}

		return false;
	}

}
