# DBSG-Backend

### **DBSG**는 듣밥세끼의 앞 자음을 영어로 딴 말로써 현재 진행중인 레시피 어플의 이름을 의미합니다.
  
**주요 기능**    
1.레시피 열람 가능    
2.원하는 레시피를 선택 후 요리를 시작하면 해당 레시피를 단계적으로 읽어주는 기능 (TTS)    
3.시간이 필요한 단계의 경우 타이머 제공    
4.타이머의 시간은 임의로 사용자가 조정 가능    
5.SNS 로그인 (카카오, 네이버)    

**테이블 생성 Query**    
<code>
1.회원정보를 저장할 USER 테이블    
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
