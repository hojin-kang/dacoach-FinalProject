package com.dacoach.page;

public class PageModule {

	public static String makePage(String pagename, int totalCnt, int listSize, int pageSize, int cp) {

		StringBuffer sb = new StringBuffer();

		int totalPage = totalCnt / listSize + 1;
		if (totalCnt % listSize == 0)
			totalPage--;

		int userGroup = cp / pageSize;
		if (cp % pageSize == 0)
			userGroup--;

		if (userGroup != 0) {
			sb.append("<a href='");
			sb.append(pagename);
			sb.append("?cp=");
			int temp = (userGroup - 1) * pageSize + pageSize;
			sb.append(temp);
			sb.append("'>&lt;&lt;</a>");
			// <a href="pageTest.jsp?cp=<%=(userGroup - 1) * pageSize +
			// pageSize%>">&lt;&lt;</a>
		}

		for (int i = userGroup * pageSize + 1; i <= userGroup * pageSize + pageSize; i++) {
			sb.append("&nbsp;&nbsp;<a href='");
			sb.append(pagename);
			sb.append("?cp=");
			sb.append(i);
			sb.append("'>");
			sb.append(i);
			sb.append("</a>&nbsp;&nbsp;");
			// <a href="pageTest.jsp?cp=<%=i%>"><%=i%></a>&nbsp;&nbsp;

			if (i == totalPage) {
				break;
			}
		}

		if (userGroup != (totalPage / pageSize) - (totalPage % pageSize == 0 ? 1 : 0)) {
			sb.append("<a href='");
			sb.append(pagename);
			sb.append("?cp=");
			int temp = (userGroup + 1) * pageSize + 1;
			sb.append(temp);
			sb.append("'>&gt;&gt;</a>");
			// <a href="pageTest.jsp?cp=<%=(userGroup + 1) * pageSize + 1%>">&gt;&gt;</a>

		}
		return sb.toString();
	}
	public static String makePagewithParams(String pagename, int totalCnt, int listSize, int pageSize, int cp) {

		StringBuffer sb = new StringBuffer();

		int totalPage = totalCnt / listSize + 1;
		if (totalCnt % listSize == 0)
			totalPage--;

		int userGroup = cp / pageSize;
		if (cp % pageSize == 0)
			userGroup--;

		if (userGroup != 0) {
			sb.append("<a href='");
			sb.append(pagename);
			sb.append("&cp=");
			int temp = (userGroup - 1) * pageSize + pageSize;
			sb.append(temp);
			sb.append("'>&lt;&lt;</a>");
			// <a href="pageTest.jsp?cp=<%=(userGroup - 1) * pageSize +
			// pageSize%>">&lt;&lt;</a>
		}

		for (int i = userGroup * pageSize + 1; i <= userGroup * pageSize + pageSize; i++) {
			sb.append("&nbsp;&nbsp;<a href='");
			sb.append(pagename);
			sb.append("&cp=");
			sb.append(i);
			sb.append("'>");
			sb.append(i);
			sb.append("</a>&nbsp;&nbsp;");
			// <a href="pageTest.jsp?cp=<%=i%>"><%=i%></a>&nbsp;&nbsp;

			if (i == totalPage) {
				break;
			}
		}

		if (userGroup != (totalPage / pageSize) - (totalPage % pageSize == 0 ? 1 : 0)) {
			sb.append("<a href='");
			sb.append(pagename);
			sb.append("&cp=");
			int temp = (userGroup + 1) * pageSize + 1;
			sb.append(temp);
			sb.append("'>&gt;&gt;</a>");
			// <a href="pageTest.jsp?cp=<%=(userGroup + 1) * pageSize + 1%>">&gt;&gt;</a>

		}
		return sb.toString();
	}

}

