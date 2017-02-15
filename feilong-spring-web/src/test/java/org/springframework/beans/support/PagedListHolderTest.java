package org.springframework.beans.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PagedListHolderTest{

    public static void main(String[] args){
        List<String> sourceList = buildSourceList();

        PagedListHolder<String> pagedListHolder = new PagedListHolder<String>(sourceList);
        pagedListHolder.setPageSize(10);//设置每一页的元素数量

        //***********************************************************************************
        //操作
        int count = pagedListHolder.getPageCount();//得到总页数
        System.out.println("总页数是：" + count);// 输出：总页数是：3

        for (int j = 0; j++ < count; pagedListHolder.nextPage()){//pagedList.nextPage()，相当于翻页。如果已经是最后一页，得到的还是最后一页
            Iterator<String> it = pagedListHolder.getPageList().iterator();//得到当前页并迭代当前页的元素
            System.out.println("*********第" + (pagedListHolder.getPage() + 1) + "页的元素开始*********");
            while (it.hasNext()){
                String str = it.next();
                System.out.print(str + "  ");
            }
            System.out.println();
            System.out.println("*********第" + (pagedListHolder.getPage() + 1) + "页的元素结束*********");
        }

        //总页数是：3
        if (pagedListHolder.isLastPage()){
            System.out.println("最后一页" + (pagedListHolder.getPage() + 1));//pagedList.getPage()得到当前页的页码，第一页为0
        }

        System.out.println(pagedListHolder.getFirstElementOnPage());//得到当前页第一个元素在list中的索引 输出：
        System.out.println(pagedListHolder.getLastElementOnPage());//得到当前页最后一个元素在list中的索引 输出：
        System.out.println(pagedListHolder.getPageCount());//输出：3

        pagedListHolder.setPage(2);//设置当前页的页数，页数从0开始
        List<String> page2 = pagedListHolder.getPageList();//得到当前页的元素
        System.out.println(page2.toString());//输出：[object20, object21, object22, object23, object24, object25, object26, object27, object28, object29]

    }

    /**
     * @return
     * @since 1.10.1
     */
    private static List<String> buildSourceList(){
        List<String> sourceList = new ArrayList<String>();
        for (int i = 0; i < 30; i++){
            sourceList.add("object" + i);
        }
        return sourceList;
    }

}