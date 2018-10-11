package cn.ioms.ssm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.ioms.ssm.service.TestService;
import cn.ioms.ssm.util.Entity;
import cn.ioms.ssm.util.ExcelTemplate;
import cn.ioms.ssm.util.ExcelUtil;

/**
 * @RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。
 */
@Controller
public class TestController {

	
	@Autowired
	private TestService testService;
	
	@RequestMapping(value = "/")
	public String  index() {
		return "index";
	}
	
	
	@RequestMapping(value = "/msg")
	public String msg() {
		return testService.msg();
	}
	
	/**
     * @Title: getExcelTemplate 
     * @Description: 生成Excel模板并导出 
     * @param @param uuid
     * @param @param request
     * @param @param response
     * @param @return
     * @return Data
     * @throws
     */
    @RequestMapping("/getExcelTemplate")
    public void getExcelTemplate(HttpServletRequest request, HttpServletResponse response){
        String fileName = "员工记录信息表"; //模板名称
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型  
        response.setContentType("multipart/form-data");  
		try {
			response.setHeader("Content-Disposition", "attachment;filename="  
					+ ExcelTemplate.encodeChineseDownloadFileName(request,fileName) +".xls");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //下拉框数据
        List<String[]> downData = new ArrayList<>();
        String[] str1 = {"男","女","未知"};
        String[] str2 = {"北京","上海","广州","深圳","武汉","长沙","湘潭"};
        String[] str3 = {"01-汉族","02-蒙古族","03-回族","04-藏族","05-维吾尔族","06-苗族","07-彝族","08-壮族","09-布依族",
        		"10-朝鲜族","11-满族","12-侗族","13-瑶族","14-白族","15-土家族","16-哈尼族","17-哈萨克族","18-傣族","19-黎族","20-傈僳族",
        		"21-佤族","22-畲族","23-高山族","24-拉祜族","25-水族","26-东乡族","27-纳西族","28-景颇族","29-柯尔克孜族","30-土族",
        		"31-达斡尔族","32-仫佬族","33-羌族","34-布朗族","35-撒拉族","36-毛难族","37-仡佬族","38-锡伯族","39-阿昌族","40-普米族",
        		"41-塔吉克族","42-怒族","43-乌孜别克族","44-俄罗斯族","45-鄂温克族","46-德昂族","47-保安族","48-裕固族","49-京族","50-塔塔尔族",
        		"51-独龙族","52-鄂伦春族","53-赫哲族","54-门巴族","55-珞巴族","56-基诺族","98-外国血统","99-其他"};
        downData.add(str1);
        downData.add(str2);
        downData.add(str3);
        Integer [] downRows = {1,5,6}; //下拉的列序号数组(序号从0开始)
        Integer [] dateRows = {4,7};	//时间文本框列
        try {
        	ExcelTemplate.createExcelTemplate(response, Entity.USERINFO_EXCEL_HANDERS, downData, downRows,dateRows);
        } catch (Exception e) {
            System.out.println("批量导出信息异常：" + e.getMessage());
        }
    }
    
    @RequestMapping(value="importExcel")
    @ResponseBody
    public String importExcel(MultipartFile file) {
    	List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    	Map<String, Object> map = null;
    	try {
            InputStream is = file.getInputStream();
            String[] listStr = ExcelUtil.readExcelTitle(is);
            
            List<String[]> list = ExcelUtil.readExcelContent(is,1);
            String[] str = null;
            for (int i = 0; i < list.size(); i++) {
            	str = list.get(i);
            	map = new HashMap<String, Object>();
            	map.put("name", str[0]);
            	map.put("sex", str[1]);
            	map.put("IDType", str[2]);
            	map.put("ID", str[3]);
            	map.put("endDate", str[4]);
            	map.put("address", str[5]);
            	map.put("race", str[6]);
            	map.put("createDate", str[7]);
            	listMap.add(map);
            }
        } catch ( IOException e) {
            e.printStackTrace();
        }
    	return JSON.toJSONString(listMap);
    }
}
