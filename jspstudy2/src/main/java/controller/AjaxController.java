package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import gdu.mskim.MskimRequestMapping;
import gdu.mskim.RequestMapping;

@WebServlet(urlPatterns = { "/ajax/*" }, initParams = { @WebInitParam(name = "view", value = "/view/") })

public class AjaxController extends MskimRequestMapping {

	@RequestMapping("select")
	public String select(HttpServletRequest request, HttpServletResponse response) {
		BufferedReader fr = null;
		// sido.txt 파일 읽기
		String path = request.getServletContext().getRealPath("/") + "file/sido.txt";
		try {
			fr = new BufferedReader(new FileReader(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// LinkedHashSet : 중복 불가, 순서 유지 만약 정렬을 하고 싶으면 treeSet
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		String si = request.getParameter("si"); // 구군 설정
		String gu = request.getParameter("gu"); // 동리 설정
		if (si == null && gu == null) { // 시도 설정
			try {
				while ((data = fr.readLine()) != null) {
					// \\s+ :정규식.공백 한 개 이상으로 분리
					String[] arr = data.split("\\s+");
					// arr[0]: 시도 해당 문자열
					if (arr.length >= 3)
						set.add(arr[0].trim());

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (gu == null) {// si 파라미터 값은 null이 아님
			si = si.trim();
			try {
				while ((data = fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if (arr.length >= 3 && arr[0].equals(si) && !arr[1].contains(arr[0]))
						set.add(arr[1].trim());

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (gu != null && si != null) {
			si = si.trim();
			gu = gu.trim();
			try {
				while ((data = fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if (arr.length >= 3 && arr[0].equals(si) && arr[1].equals(gu) && !arr[1].contains(arr[0])
							&& !arr[2].contains(arr[1])) {
						if (arr.length > 3) {
							if (arr[3].contains(arr[1]))
								continue;
							arr[2] += " " + arr[3];
						}
						set.add(arr[2].trim()); // 동리 정보 설정
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		request.setAttribute("list", new ArrayList<String>(set));
		request.setAttribute("len", set.size());
		return "ajax/select";
	}

	@RequestMapping("exchange")
	public String exchange(HttpServletRequest request, HttpServletResponse response) {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>();
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr");
			//p.table-unite : <p class="table-unit">인 태그
			exdate = doc.select("p.table-unit").html();
			for (Element tr : trs) {
				List<String> tdlist = new ArrayList<>();
				Elements tds = tr.select("td");
				for (Element td : tds) {
					tdlist.add(td.html()); //0번 인덱스 : 통화코드, 1:통화명,2:받으실 때, 3:보내실 때, 4:매매 기준율
				}
				if(tdlist.size() > 0) {
				  if (tdlist.get(0).equals("USD")
						|| tdlist.get(0).equals("CNH") 
						|| tdlist.get(0).equals("JPY(100)")
						|| tdlist.get(0).equals("EUR")) {
					     trlist.add(tdlist);
				}

			}
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
    System.out.println(trlist);		
	request.setAttribute("date", exdate);
	request.setAttribute("list", trlist);
	return "ajax/exchange";
	}
}