

<img src="https://www.millie.co.kr/favicon/millie_og.png" height="100"/>

# millie(밀리의 서재) REST API

[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/ohahohah/readme-template/graphs/commit-activity)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![spring-boot](http://img.shields.io/badge/-springBoot-0072b1?style=flat&logo=spring)](https://spring.io/projects/spring-boot)


> 책들을 등록하고, 읽고, 독서 노트에 기록해보세요!

> REST API 
## 핵심 기능  Key Feature
- 2호선 역을 검색하면, 카페 정보가 카드형태로 보여집니다. (Search station name, then we will show you cafes in card shape)
- 2호선 역 근처, 나만 아는 카페를 등록하실 수 있습니다. (You can upload cafe that you think it`s special)

## 사용 How To Use
- 사용하고 싶도록 유용한 몇 가지 예를 적습니다. 코드 블록과 스크린샷 포함.
- (wiki가 있을 경우) _더 많은 예시와 사용 예제는 [Wiki](wiki)를 참고하세요._
  ![](header.png)

## API
Index | Method | URI | Description
---|------|-----|---|
1	|get	|/collection	|메인 책 컬렉션 보기
2	|get	|/collection/{collection_id}	|특정 컬렉션 보기
3	|get	|/books?category=	|카테고리 별 목록 보기
4	|get	|/books/{book_id}	|특정 도서 보기
5	|get	|/shelfs/books	|내 책장 목록 보기(history)
6	|get	|/shelfs/books?sequence=	|특정 조건 순서로 책 목록 보기
7	|get	|/shelfs	|책장 별 데이터 보기
8	|get	|/shelfs/{shelf_id}/books	|책장 별 책 보기
9	|post	|/shelfs	|특정 책장 생성
10	|post	|/shelfs/{shelf_id}/books	|특정 책장에 책 추가
11	|patch	|/shelfs/{shelf_id}/books	|특정 책장에서 책 삭제
12	|post	|/users	|회원가입
13	|get	|/users	|개인 유저 정보 조회 (서재 화면)
14	|post	|/users/login	|로그인
15	|get	|/posts	|독서 노트 목록 보기
16	|post	|/posts	|독서 노트 등록
17	|get	|/posts/{post_id}	|특정 노트 읽기
18	|put	|/posts/{post_id}	|독서 노트 수정
19	|patch	|/posts/{post_id}	|독서 노트 삭제
20	|post	|/posts/{post_id}/likes	|독서 노트 좋아요 및 좋아요 취소
21	|get	|/posts/{post_id}/comments	|코멘트 불러오기
22	|post	|/posts/{post_id}/comments	|코멘트 작성하기
23	|patch	|/posts/{post_id}/comments/{comments_id}	|코멘트 삭제하기
24	|put	|/posts/{post_id}/comments/{comments_id}	|코멘트 수정하기
25	|post	|/users/follow/{user}	|팔로잉 등록 및 취소



## Reference

## Links
- API 명세서를 살펴보세요!
  https://docs.google.com/spreadsheets/d/1E_4B-z-t66OLv1BpfBzKFsFppD8kyX68kPhoruOhQsU/edit?usp=sharing



