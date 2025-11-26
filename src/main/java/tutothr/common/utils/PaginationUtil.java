package tutothr.common.utils;

import java.util.List;

public class PaginationUtil<T> {

	
	private int size=5;
	private int page=1;
	private int totalPages;
	private List<T> records;
	
	
	public PaginationUtil(int size, int page, int totalPages, List<T> records) {
		super();
		this.size = size;
		this.page = page;
		this.totalPages = totalPages;
		this.records = records;
	}


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getTotalPages() {
		return totalPages;
	}


	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}


	public List<T> getRecords() {
		return records;
	}


	public void setRecords(List<T> records) {
		this.records = records;
	}
	
	
	
	
	
			
}
