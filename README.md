[팀 프로젝트] DB 없이 바로 실행되는 '인메모리 블로그 시스템'

1. 프로젝트 소개 (Overview)
2. 
• 목적: DB 설치/설정 없이 실행만 하면 즉시 작동하는 'Zero-Config' 블로그 구현.
• 핵심: Java의 자료구조(Map, List)만을 활용해 RDBMS 수준의 데이터 관계를 메모리상에 구축했음.

3. 기술 스택 (Tech Stack)

• Framework: Spring Boot 3.x, Java 17
• View: Thymeleaf, Layout Dialect
• Storage: Java Collections (ConcurrentHashMap, AtomicLong)
• Utility: LoremIpsum (더미 데이터 자동 생성용)

3. 주요 기능 (Key Features)

데이터 관리: 자바 컬렉션 기반의 인메모리 저장소를 구축하여 빠른 데이터 처리 속도를 확보했음.
카테고리: 일괄 편집 기능 제공 및 삭제 시 '미분류'로 자동 이동해 데이터 보호했음.
대댓글: 부모 ID 기반의 무제한 트리 구조를 재귀 로직으로 구현했음.
페이징: 오프셋 계산을 통해 '이전/다음' 버튼으로 작동하는 슬라이싱 방식 적용했음.
