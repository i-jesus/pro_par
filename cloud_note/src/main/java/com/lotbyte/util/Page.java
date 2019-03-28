package com.lotbyte.util;

import java.util.List;


public class Page<T> {
	
	private Integer pageNum = 1;//当前页
	private Integer pageSize = 10;//每页记录数
	private Integer total;//总记录数
	private Integer totalPages;// 总页数
	private Integer firstPage;//首页
	private Integer endPage;//末页
	private Integer prePage;//上一页
	private Integer nextPage;//下一页
	private Integer navStartPage;// 导航开始页
	private Integer navEndPage;//导航结束页
	private List<T>  datas;//当前页记录
	
	
	
	
	public Page(Integer pageNum, Integer pageSize, Integer total) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.total = total;
		
		// 计算总页数   Math.ceil:向上取整
		this.totalPages = (int) Math.ceil(total * 1.0 / pageSize);
//		this.totalPages = (total + pageSize -1) / pageSize;
		
		// 指定首页 末页
		this.firstPage = 1;
		this.endPage = this.totalPages;
		
		// 上一页  下一页计算
		this.prePage=(pageNum-1) <= 0?1:(pageNum-1);
		this.nextPage=(pageNum+1) > totalPages?totalPages:(pageNum+1);
		
		// 导航开始页  导航结束页   
		this.navStartPage = pageNum - 5;  
		this.navEndPage = pageNum + 4; 
		// 修正导航开始页  导航结束页值
		if(this.navStartPage <= 0){
			this.navStartPage = 1; 
			this.navEndPage = (this.navStartPage+9)>totalPages?totalPages:(this.navStartPage+9);
		}
		
		if(this.navEndPage > totalPages){
			this.navEndPage = totalPages; 
			this.navStartPage = (this.navEndPage-9)<=0?1:(this.navEndPage-9); 
		}
	}
	public Page() {
		super();
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public Integer getFirstPage() {
		return firstPage;
	}
	public void setFirstPage(Integer firstPage) {
		this.firstPage = firstPage;
	}
	public Integer getEndPage() {
		return endPage;
	}
	public void setEndPage(Integer endPage) {
		this.endPage = endPage;
	}
	public Integer getPrePage() {
		return prePage;
	}
	public void setPrePage(Integer prePage) {
		this.prePage = prePage;
	}
	public Integer getNextPage() {
		return nextPage;
	}
	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}
	public Integer getNavStartPage() {
		return navStartPage;
	}
	public void setNavStartPage(Integer navStartPage) {
		this.navStartPage = navStartPage;
	}
	public Integer getNavEndPage() {
		return navEndPage;
	}
	public void setNavEndPage(Integer navEndPage) {
		this.navEndPage = navEndPage;
	}
	public List<T> getDatas() {
		return datas;
	}
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
	
	
	
	

}
