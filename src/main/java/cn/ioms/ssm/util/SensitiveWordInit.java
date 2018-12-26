package cn.ioms.ssm.util;

import cn.ioms.ssm.entity.SensitiveWord;
import cn.ioms.ssm.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 默认在该类内@Autowired 注解是无效的，通过以下三步即可实现在该类内调用Service 方法
 */
@Component  //关键1，将该工具注册为组件
public class SensitiveWordInit {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    public HashMap sensitiveWordMap;

    public static SensitiveWordInit sensitiveWordInit;// 关键2

    // 关键3
    @PostConstruct
    public void init() {
        sensitiveWordInit = this;
        sensitiveWordInit.sensitiveWordService = this.sensitiveWordService;
    }

    public SensitiveWordInit(){
        super();
    }

    public Map initKeyWord(){
        try {
            //读取敏感词库
            Set<String> keyWordSet = dbSensitiveWord();
            //将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
            //spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = {
     *      isEnd = 0
     *      国 = {<br>
     *      	 isEnd = 1
     *           人 = {isEnd = 0
     *                民 = {isEnd = 1}
     *                }
     *           男  = {
     *           	   isEnd = 0
     *           		人 = {
     *           			 isEnd = 1
     *           			}
     *           	}
     *           }
     *      }
     *  五 = {
     *      isEnd = 0
     *      星 = {
     *      	isEnd = 0
     *      	红 = {
     *              isEnd = 0
     *              旗 = {
     *                   isEnd = 1
     *                  }
     *              }
     *      	}
     *      }
     */
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()){
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMap;
            for(int i = 0 ; i < key.length() ; i++){
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if(wordMap != null){        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                }
                else{     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String,String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if(i == key.length() - 1){
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     * @return
     */
    private Set<String> dbSensitiveWord() {
        //查询敏感词
        List<SensitiveWord> list = sensitiveWordInit.sensitiveWordService.queryAll();
        Set<String> set = new HashSet<String>();
        if (list != null && list.size() > 0){
            for (SensitiveWord sen:list) {
                set.add(sen.getValue());
            }
        }
        return set;
    }

}
