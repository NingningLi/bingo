<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html;charset=GBK" />
<link type="text/css" rel="stylesheet" href="./css/bingo.css"/>
<title>�������Bingo!</title>
</head>
<body>
<div id="container">
    <div style="height:70px"></div>
    <div class="logo"> 
       <span class="hook"></span> 
       <img src="./images/bingo_index.jpg" />
      </div>
      
     <div style="height:20px"></div>
     
     <div id="searchform" >
          <form name="searchForm" action="servlet/SearchServlet" method="post">
		  <div id=radio_container>
		  <input type="radio" name="searchType" value = "news" />����
	      <input type="radio" name="searchType" value = "webPage" Checked/>��ҳ
	      <input type="radio" name="searchType" value = "music" />����
	      <input type="radio" name="searchType" value = "image" />ͼƬ
          <input type="radio" name="searchType" value = "video" />��Ƶ
	      </div>
	      <div class="searchinput">
              <input class="s_input" type="text" name="keyWords" />
              <input class="submit s_btn" type="submit" name="submit" value="�͹�����" />
          </div>
          </form>
      </div>
      
      <div id="info_container">
          <font size="3">��ѧ|����|����|����</font>
          <P><font size="3"><a href="http://www.ujn.edu.cn/" class="hover{color:#ff0000;}">���ϴ�ѧ</a>-<a href="http://ise.ujn.edu.cn/">��Ϣ��ѧ�빤��ѧԺ</a></font>
      </div>
      <div style="height:60px"></div>
      <div id="footbanner">
      <p id="copyright">Copyright &copy; 2014 <a href="http://www.cnblogs.com/binyue/">��Խ</a>-<a href="#">�͹���������</a>-<a href="mailto:mail@ibingyue.cn">������ϵ</a></p>
      </div>
</div>
</body>
</html>
     
     