# 310-126 OOP: repo ของรายวิชา (ใช้ทั้งภาค)

repo นี้สร้างจาก template ของรายวิชา 310-126 OOP (2/69) ทุก Lab และ Assignment อยู่ใน repo นี้ แยกเป็นโฟลเดอร์ย่อย
ขั้นตอนเต็มใน [Lab 0 handout](https://github.com/kasikrit/310-126-OOP-2-69-student/blob/main/Lab0-handout.md)

**ครั้งแรกครั้งเดียว**
1. ชื่อ repo ต้องเป็น `310-126-oop` (private)
2. เพิ่ม collaborator 3 บัญชี: `kasikrit` (อาจารย์) + TA 2 คน `tonethana` และ `kroekchanwit` (Settings → Collaborators → Add people) **ห้ามลบคำเชิญจนจบภาคการศึกษา**
3. `javac NewTask.java`

**ทุกงาน** (เช่น Assignment 1 ใช้โฟลเดอร์ `Assignment1` ตามที่ประกาศพร้อมโจทย์)
1. ที่ root ของ repo: `java NewTask Assignment1` จะได้โฟลเดอร์ `Assignment1/` ที่มี `PROCESS.md`, `ai-log/`, `Run.java`, `outputs.txt`
2. `cd Assignment1` กรอก `PROCESS.md` ขั้น 1 แล้ว commit + push **ก่อนเปิด AI และก่อนเขียนโค้ด**
3. ใช้ AI แบบแชตกับ tutor prompt และเก็บบทสนทนาใน `ai-log/` เป็น `.md` หรือ PDF (ไม่ใช้ AI: `ai-log/NONE.md` และต้อง viva)
4. compile ด้วย `javac *.java` แล้วรันด้วย `java Run <ชื่อคลาส>` เท่านั้น (ผลถูกบันทึกลง `outputs.txt` เอง ห้ามแก้ `outputs.txt` และ `Run.java`) และ commit + push หลังรันแต่ละครั้ง
5. ติ๊กคำรับรองท้าย `PROCESS.md` ใน commit สุดท้าย แล้วส่ง URL ของ repo นี้ + SHA เต็ม 40 ตัว (`git rev-parse HEAD`) ผ่าน Google Form ส่งงานภายใน deadline

**ไฟล์ที่ root:** `NewTask.java` สร้างโฟลเดอร์งาน, `_template/` ต้นแบบของโฟลเดอร์งาน (ห้ามแก้), `Migrate.java` ใช้เฉพาะคนที่ย้ายจาก repo `310-126-lab0` เดิม (ดู handout)
