# DBSG-Backend

### **DBSG**는 듣밥세끼의 앞 자음을 단어로 딴 단어로 현재 진행중인 레시피 어플의 이름을 의미합니다.
>이 repository에서는 해당 어플의 Backend를 담당하는 코드를 포함하고 있습니다.       
>서버가 불안정해 가끔 다운되는 경우가 있으니 양해바랍니다.


**Web URL**     
http://15.165.215.38:8080/DBSG/     


**사용 기술**      
1.언어:JAVA(Spring Framework)     
2.DB:MySQL     
3.Server:AWS(Centos7)     

   
**어플 주요 기능**    
1.레시피 열람 가능    
2.원하는 레시피를 선택 후 요리를 시작하면 해당 레시피를 단계적으로 읽어주는 기능 (TTS)    
3.시간이 필요한 단계의 경우 타이머 제공    
4.타이머의 시간은 임의로 사용자가 조정 가능    
5.SNS 로그인 (카카오, 네이버)    


**Backend에 진행되어져 있는 기능**    
1.메뉴/레시피 등록      

2.전체 메뉴 조회     
http://15.165.215.38:8080/DBSG/menu/list     
<pre>
<code>
{
    "menuList": "success",
    "con": "success",
    "size": 3,
    "data": [
        {
            "menu_no": 1,
            "menu_name": "첫 메뉴",
            "menu_image": null,
            "menu_description": "ㅁㄴㅅㅁ",
            "menu_category": 0,
            "menu_reqMaterial": "ㅍㅅㅈㄹ",
            "menu_needlessMaterial": "ㅅㅌㅈㄹ",
            "menu_star": 0.0,
            "menu_tag": "1,2,3",
            "menu_totalTime": 180,
            "menu_writer": null,
            "menu_preferCnt": 0,
            "menu_readCnt": 36
        },
        {
            "menu_no": 2,
            "menu_name": "메뉴2",
            "menu_image": "",
            "menu_description": "",
            "menu_category": 0,
            "menu_reqMaterial": "공기",
            "menu_needlessMaterial": "",
            "menu_star": 0.0,
            "menu_tag": "11,28,42",
            "menu_totalTime": 540,
            "menu_writer": null,
            "menu_preferCnt": 0,
            "menu_readCnt": 30
        },
        {
            "menu_no": 3,
            "menu_name": "메뉴3",
            "menu_image": "",
            "menu_description": "",
            "menu_category": 0,
            "menu_reqMaterial": "산소",
            "menu_needlessMaterial": "마우스",
            "menu_star": 0.0,
            "menu_tag": "17,5,37",
            "menu_totalTime": 600,
            "menu_writer": null,
            "menu_preferCnt": 0,
            "menu_readCnt": 12
        }
    ]
}
</code>
</pre>

3.문자열로 메뉴 검색    
검색한 문자열을 메뉴 이름, 메뉴 설명, 필요재료에서 검색해서 해당되는 메뉴 출력          
http://15.165.215.38:8080/DBSG/menu/search/%EC%B2%AB      
#데이터가 존재하는 경우
<pre>
<code>
{
    "menuList": "success",
    "con": "success",
    "data": [
        {
            "menu_no": 1,
            "menu_name": "첫 메뉴",
            "menu_image": null,
            "menu_description": "ㅁㄴㅅㅁ",
            "menu_category": 0,
            "menu_reqMaterial": "ㅍㅅㅈㄹ",
            "menu_needlessMaterial": "ㅅㅌㅈㄹ",
            "menu_star": 0.0,
            "menu_tag": "1,2,3",
            "menu_totalTime": 180,
            "menu_writer": null,
            "menu_preferCnt": 0,
            "menu_readCnt": 36
        }
    ],
    "size": 1,
    "keyword": "첫"
}
</code>
</pre>
#데이터가 존재하지 않는 경우
<pre>
<code>
{
    "menuList": "success",
    "con": "success",
    "data": [],
    "size": 0,
    "keyword": "뷁"
}
</code>
</pre>                  

4.태그번호로 메뉴 검색    
입력한 태그번호를 가진 메뉴만 출력           
http://15.165.215.38:8080/DBSG/menu/tag/11      
#존재하는 태그로 검색 시 (1)
<pre>
<code>
{
    "con": "success",
    "size": 1,
    "data": [
        {
            "menu_no": 1,
            "menu_name": "첫 메뉴",
            "menu_image": null,
            "menu_description": "ㅁㄴㅅㅁ",
            "menu_category": 0,
            "menu_reqMaterial": "ㅍㅅㅈㄹ",
            "menu_needlessMaterial": null,
            "menu_star": 0.0,
            "menu_tag": "1,2,3",
            "menu_totalTime": 180,
            "menu_writer": null,
            "menu_preferCnt": 0,
            "menu_readCnt": 36
        }
    ],
    "tagSearch": "success",
    "keyword": 1
}
</code>
</pre>       
#존재하지 않는 태그로 검색시 (100)  
<pre>
<code>
{
    "con": "success",
    "size": 0,
    "data": [],
    "tagSearch": "success",
    "keyword": 100
}
</code>
</pre>              

5.조회수가 많은 메뉴 추천    
전체메뉴 중에서 조회수(readCnt)가 높은 순서대로 3개          
http://15.165.215.38:8080/DBSG/menu/recommend      
<pre>
<code>
{
    "con": "success",
    "data": [
        {
            "menu_no": 2,
            "menu_name": "메뉴2",
            "menu_image": "",
            "menu_description": "",
            "menu_category": 0,
            "menu_reqMaterial": "공기",
            "menu_needlessMaterial": "",
            "menu_star": 0.0,
            "menu_tag": "11,28,42",
            "menu_totalTime": 540,
            "menu_writer": null,
            "menu_preferCnt": 30,
            "menu_readCnt": 50
        },
        {
            "menu_no": 1,
            "menu_name": "첫 메뉴",
            "menu_image": null,
            "menu_description": "ㅁㄴㅅㅁ",
            "menu_category": 0,
            "menu_reqMaterial": "ㅍㅅㅈㄹ",
            "menu_needlessMaterial": "ㅅㅌㅈㄹ",
            "menu_star": 0.0,
            "menu_tag": "1,2,3",
            "menu_totalTime": 180,
            "menu_writer": null,
            "menu_preferCnt": 10,
            "menu_readCnt": 36
        },
        {
            "menu_no": 3,
            "menu_name": "메뉴3",
            "menu_image": "",
            "menu_description": "",
            "menu_category": 0,
            "menu_reqMaterial": "산소",
            "menu_needlessMaterial": "마우스",
            "menu_star": 0.0,
            "menu_tag": "17,5,37",
            "menu_totalTime": 600,
            "menu_writer": null,
            "menu_preferCnt": 17,
            "menu_readCnt": 12
        }
    ],
    "size": 3,
    "readCntRecommend": "success"
}
</code>
</pre>            

6.메뉴 삭제    
메뉴 번호로 해당 메뉴를 삭제        
실제로 DB에서 삭제하지는 않고 나타나지 않게 처리        

7.(로그인 회원)메뉴 즐겨찾기에 등록         

8.(로그인 회원)즐겨찾기 한 메뉴 삭제          

9.즐겨찾기 수가 많은 메뉴 추천     
전체 메뉴 중 즐겨찾기 수(preferCnt)가 가장 많은 메뉴 3개 
http://15.165.215.38:8080/DBSG/prefer/recommend      
<pre>
<code>
{
    "con": "success",
    "data": [
        {
            "menu_no": 2,
            "menu_name": "메뉴2",
            "menu_image": "",
            "menu_description": "",
            "menu_category": 0,
            "menu_reqMaterial": "공기",
            "menu_needlessMaterial": null,
            "menu_star": 0.0,
            "menu_tag": "11,28,42",
            "menu_totalTime": 540,
            "menu_writer": null,
            "menu_preferCnt": 30,
            "menu_readCnt": 30
        },
        {
            "menu_no": 3,
            "menu_name": "메뉴3",
            "menu_image": "",
            "menu_description": "",
            "menu_category": 0,
            "menu_reqMaterial": "산소",
            "menu_needlessMaterial": null,
            "menu_star": 0.0,
            "menu_tag": "17,5,37",
            "menu_totalTime": 600,
            "menu_writer": null,
            "menu_preferCnt": 17,
            "menu_readCnt": 12
        },
        {
            "menu_no": 1,
            "menu_name": "첫 메뉴",
            "menu_image": null,
            "menu_description": "ㅁㄴㅅㅁ",
            "menu_category": 0,
            "menu_reqMaterial": "ㅍㅅㅈㄹ",
            "menu_needlessMaterial": null,
            "menu_star": 0.0,
            "menu_tag": "1,2,3",
            "menu_totalTime": 180,
            "menu_writer": null,
            "menu_preferCnt": 10,
            "menu_readCnt": 36
        }
    ],
    "size": 3,
    "preferRecommend": "success"
}
</code>
</pre>             

10.해당 메뉴의 상세 정보 및 레시피 내용 조회    
메뉴 번호로 해당 메뉴의 레시피와 해당 메뉴의 상세정보 조회                 
http://15.165.215.38:8080/DBSG/recipe/1      
#존재하는 메뉴
<pre>
<code>
{
    "con": "success",
    "data": {
        "menuInfo": {
            "menu_no": 1,
            "menu_name": "첫 메뉴",
            "menu_image": null,
            "menu_description": "ㅁㄴㅅㅁ",
            "menu_category": 0,
            "menu_reqMaterial": "ㅍㅅㅈㄹ",
            "menu_needlessMaterial": "ㅅㅌㅈㄹ",
            "menu_star": 0.0,
            "menu_tag": "1,2,3",
            "menu_totalTime": 180,
            "menu_writer": null,
            "menu_preferCnt": 0,
            "menu_readCnt": 35
        },
        "recipe": [
            {
                "recipe_no": 1,
                "menu_no": 1,
                "recipe_processNo": 1,
                "recipe_action": "순서1",
                "recipe_fire": 0,
                "recipe_reqTime": 60,
                "recipe_image": null
            },
            {
                "recipe_no": 2,
                "menu_no": 1,
                "recipe_processNo": 2,
                "recipe_action": "순서2",
                "recipe_fire": 2,
                "recipe_reqTime": 120,
                "recipe_image": null
            }
        ]
    },
    "size": 2,
    "recipe": "success",
    "keyword": 1
}
</code>
</pre>
#존재하지 않는 메뉴
<pre>
<code>
{
    "con": "success",
    "recipe": "fail",
    "error": "Invalid menu_no",
    "keyword": 100
}
</code>
</pre>                  

11.카카오톡 로그인 API        
http://15.165.215.38:8080/DBSG        

12.네이버 아이디로 로그인 API    
http://15.165.215.38:8080/DBSG      


**추가예정**     
1.이미지 처리      


**테이블 생성 Query**    
1.회원정보를 저장할 USER 테이블  
<pre>
<code>  
create table user(    
user_no bigint auto_increment comment '회원번호',    
user_email varchar(50) not null comment '회원이메일',    
user_nickname varchar(30) comment '회원닉네임',    
user_age varchar(7) comment '회원연령대',    
user_gender int comment '회원 성별, 1 남자 2 여자',    
user_birthday varchar(5) comment '회원 생일',    
user_image text comment '회원 이미지',    
user_type varchar(10) not null comment '회원가입한 sns',    
user_level int default 1 comment '회원 레벨, 관리자 0',    
user_delete date comment '회원삭제 여부, 기본 null, 삭제된 일자로부토 3개월 후 회원데이터 삭제',    
etc text comment '여분컬럼',    
primary key(user_no,user_nickname)    
)default character set=utf8;    
</code>
</pre>

2.해당 메뉴에 대한 전체적인 정보를 저장할 MENU 테이블
<pre>
<code>
create table menu(
menu_no bigint auto_increment comment '메뉴번호',
menu_name varchar(100) comment '메뉴이름',
user_nickname varchar(30) comment '메뉴등록자',
menu_tag text not null comment '메뉴태그번호',
menu_reqMaterial text comment '필수재료',
menu_needlessMaterial text comment '선택재료',
menu_description text comment '메뉴설명',
menu_image text comment '메뉴사진 위치',
menu_kids varchar(1) default 'N' comment '아기섭취 가능여부',
menu_totalTime int default 0 comment '총 소요 시간',
menu_delete int default 0 comment '메뉴 삭제 여부, 1이면 삭제된 메뉴',
etc text comment '여분 컬럼',
primary key(menu_no,menu_name)
)default character set=utf8;
</code>
</pre>

3.메뉴 정보 중 많이 변경될 정보를 저장해 둔 MENU_STAT 테이블
<pre>
<code>
create table menu_stat(
menu_no bigint comment '메뉴번호',
menu_star decimal(2,1) default 0 comment '메뉴평점',
menu_preferCnt int default 0 comment '메뉴 즐겨찾기 횟수',
menu_readCnt int default 0 comment '메뉴 조회수',
etc text comment '여분컬럼',
foreign key(menu_no) references menu(menu_no) on delete cascade
)default character set=utf8;
</code>
</pre>

4.회원의 즐겨찾기 메뉴 정보를 저장하 둘 PREFER 테이블
<pre>
<code>
create table prefer(
prefer_no bigint auto_increment comment '인덱스',
user_no bigint comment '회원번호',
menu_no bigint comment '메뉴번호',
prefer_regdate timestamp default now() comment '즐겨찾기 날짜',
etc text comment '여분컬럼',
primary key(prefer_no),
foreign key(user_no) references user(user_no) on delete cascade,
foreign key(menu_no) references menu(menu_no) on delete cascade
)default character set=utf8;
</code>
</pre>

5.각각의 메뉴에 대한 레시피 내용을 저장해 둘 RECIPE 테이블
<pre>
<code>
create table recipe(
recipe_no bigint auto_increment comment '인덱스',
menu_no bigint comment '메뉴번호',
recipe_processNo int comment '요리순서',
recipe_action text comment '요리내용',
recipe_fire int default 0 comment '불 세기',
recipe_reqTime int default 0 comment '해당 작업 소요 시간',
recipe_image text comment '요리이미지',
etc text comment '여분컬럼',
primary key(recipe_no),
foreign key(menu_no) references menu(menu_no) on delete cascade
)default character set=utf8;
</code>
</pre>

**타 팀원의 Github**     
어플 담당      
<https://github.com/yh-park02/DBSG_Project>
