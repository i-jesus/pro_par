package com.lotbyte.service;


import com.lotbyte.dao.UserDaoRepository;
import com.lotbyte.po.User;
import com.lotbyte.util.MD5Util;
import com.lotbyte.util.StringUtil;
import com.lotbyte.vo.ResultInfo;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Optional;

/**
 * 用户模块的业务逻辑处理
 *
 * @author Administrator
 */
public class UserService {

    private UserDaoRepository userRepository = new UserDaoRepository();

    /**
     * 用户登录
     * <p>
     * 1、判断参数是否为空，如果为空，返回错误信息
     * 2、先将前台传过来的密码加密，再去查询
     * 3、调用Dao层，通过用户名和密码去数据库中查询用户对象，得到用户对象user
     * 4、判断用户user对象是否为空，为空返回错误信息
     * 5、如果不为空，登录成功
     *
     * @param uname
     * @param upwd
     * @return
     */
    public ResultInfo userLogin(String uname, String upwd) {
        ResultInfo resultInfo = new ResultInfo();
        Optional<User> userOptional = userRepository.findUserByUnameAndUpwd(uname, MD5Util.encode(upwd));
        return userOptional.map((u) -> {
            resultInfo.setCode(1);
            resultInfo.setMsg("登录成功！");
            resultInfo.setResult(u);
            return resultInfo;
        }).orElseGet(() -> {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户名或密码不正确！");
            resultInfo.setResult(Optional.empty());
            return resultInfo;
        });
    }

    /**
     * 验证昵称的唯一性
     * 1、判断参数
     * 2、调用Dao层的查询方法，返回true和false，true代表可用，false不可用（参数：nick、userId）
     * 3、判断昵称是否可用，可用：msg=昵称未占用，可以使用！不可用：msg=昵称已被占用，不可使用！
     *
     * @param nick
     * @param userId
     * @return
     */
    public ResultInfo checkNick(String nick, Integer userId) {
        ResultInfo resultInfo = new ResultInfo();
        // 判断参数
        return Optional.ofNullable(nick).filter(StringUtils::isBlank).map((n) -> {
            resultInfo.setCode(0);
            resultInfo.setMsg("用户昵称不能为空！");
            return resultInfo;
        }).orElseGet(()->{
            Optional<User> userOptional= userRepository.checkNick(nick, userId);
             return userOptional.map(u -> {
                 resultInfo.setCode(0);
                 resultInfo.setMsg("昵称被占用，不可使用！");
                 return resultInfo;
            }).orElseGet(() -> {
                 resultInfo.setCode(1);
                 resultInfo.setMsg("昵称合法");
                 return resultInfo;
            });
        });
    }

    /**
     * 修改用户信息
     *
     * @return
     */
    public ResultInfo updateInfo(HttpServletRequest request) {

        // 接收参数
        String nick = request.getParameter("nick");
        String mood = request.getParameter("mood");

        // 得到user对象
        User user = (User) request.getSession().getAttribute("user");
        Integer userId = user.getUserId();

        User u = new User();
        u.setNick(nick);
        u.setMood(mood);
        u.setUserId(userId);

        // ==============上传头像=========
        /*try {
            // 得到part对象，name是file元素的name属性值
            Part part = request.getPart("img");
            // 得到文件上传的名称
            String fileName = part.getName();
            System.out.println("文件名:" + fileName);
            // 判断是否上传了头像
            if (!StringUtil.isNullOrEmpty(fileName)) {
                u.setHead(fileName);
                // 上传文件的存放路径
                String path = request.getServletContext().getRealPath("/upload/" + fileName);
                part.write(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        ResultInfo resultInfo = new ResultInfo();
        // 调用Dao层的修改方法，返回受影响的行数
        Integer row = userRepository.updateInfo(u);
        return Optional.ofNullable(row).filter(r->r>0).map((r)->{
            resultInfo.setCode(1);
            resultInfo.setMsg("修改成功！");
            //修改后user
            resultInfo.setResult(user);
            return resultInfo;
        }).orElseGet(()->{
            resultInfo.setCode(0);
            resultInfo.setMsg("修改失败！");
            // 元素user
            resultInfo.setResult(request.getSession().getAttribute("user"));
            return resultInfo;
        });
    }



}
