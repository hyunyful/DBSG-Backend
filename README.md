# DBSG-Backend

### **DBSG**는 듣밥세끼의 앞 자음을 영어로 딴 말로써 현재 진행중인 레시피 어플의 이름을 의미합니다.
>이 repository에서는 해당 어플의 Backend를 담당하는 코드를 포함하고 있습니다.
  
**주요 기능**    
1.레시피 열람 가능    
2.원하는 레시피를 선택 후 요리를 시작하면 해당 레시피를 단계적으로 읽어주는 기능 (TTS)    
3.시간이 필요한 단계의 경우 타이머 제공    
4.타이머의 시간은 임의로 사용자가 조정 가능    
5.SNS 로그인 (카카오, 네이버)    

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
