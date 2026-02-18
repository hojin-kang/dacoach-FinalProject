package com.dacoach.service.company;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dacoach.mapper.chat.ChatMapper;
import com.dacoach.mapper.company.CompanyMapper;
import com.dacoach.model.chat.ChatMessageDTO;
import com.dacoach.model.chat.ChatRoomDTO;
import com.dacoach.model.classes.ClassDTO;
import com.dacoach.model.coachClasses.ClassEnrollmentDTO;
import com.dacoach.model.company.CertDTO;
import com.dacoach.model.company.CompanyDTO;
import com.dacoach.model.company.CompanyProvideDTO;
import com.dacoach.model.company.CompanyRegionDTO;
import com.dacoach.model.users.UsersDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImple implements CompanyService {
	
	 private final ChatMapper chatMapper;

	    private static final String CHAT_DIR = "C:/student_java/dacoach/dacoach/uploads/chats";
	    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	    private static final Charset[] READ_CHARSETS = new Charset[] {
	            StandardCharsets.UTF_8,
	            Charset.forName("MS949"),
	            Charset.forName("CP949")
	    };
	    
	@Autowired
	private CompanyMapper companyMapper;
	
	@Override
    @Transactional
    public ChatMessageDTO saveAndBuildBroadcast(ChatMessageDTO msg) {
        if (msg == null) return null;

        int roomIdx = msg.getRoomIdx();
        int senderIdx = msg.getSenderIdx();
        String message = (msg.getMessage() == null) ? "" : msg.getMessage();

        // 권한 체크 + user1/user2 확보
        ChatRoomDTO room = chatMapper.selectRoomByIdx(roomIdx, senderIdx);
        if (room == null) {
        	  try {
                  room=companyMapper.getClassRoom(roomIdx);
               } catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
              }
        

        // 폴더 생성
        Path dir = Paths.get(CHAT_DIR);
        try { Files.createDirectories(dir); } catch (IOException ignored) {}

        // "기존에 있던 파일"을 우선으로 잡아서 append (파일 2개 생기는 거 방지)
        Path filePath = resolveLegacyChatFilePath(room);
        if (filePath == null) {
            // 둘 다 없으면 새로 만들 때는 확장자 없는 기본 이름 사용 (원하면 .txt로 바꿔도 됨)
            String base = legacyBaseName(room);
            filePath = dir.resolve(base);
        }

        // 한 줄 저장: yyyy-MM-dd HH:mm:ss|senderIdx|message
        String ts = LocalDateTime.now().format(TS_FMT);
        String clean = message.replace("\r", " ").replace("\n", " ");
        String line = ts + "|" + senderIdx + "|" + clean + "\n";

        try {
            Files.writeString(filePath, line, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            return null;
        }

        // DB 미리보기/시간 업데이트
        String preview = clean.length() > 200 ? clean.substring(0, 200) : clean;
        chatMapper.updateRoomLast(roomIdx, preview);

        // 상대 unread +1
        chatMapper.increaseUnreadOther(roomIdx, senderIdx);

        // 브로드캐스트용
        ChatMessageDTO out = new ChatMessageDTO();
        out.setRoomIdx(roomIdx);
        out.setSenderIdx(senderIdx);
        out.setMessage(clean);
        out.setTs(ts);
        return out;
    }
	 private String legacyBaseName(ChatRoomDTO room) {
	        int a = room.getUser1Idx();
	        int b = room.getUser2Idx();
	        int min = Math.min(a, b);
	        int max = Math.max(a, b);
	        return "chat_" + min + "_" + max;
	    }
	 private Path resolveLegacyChatFilePath(ChatRoomDTO room) {
	        Path dir = Paths.get(CHAT_DIR);

	        String base = legacyBaseName(room);

	        Path p1 = dir.resolve(base);
	        if (Files.exists(p1)) return p1;

	        Path p2 = dir.resolve(base + ".txt");
	        if (Files.exists(p2)) return p2;

	        return null;
	    }
	 
	 public String loadMessagesRawCustom(int roomIdx, int myIdx) {
		    // 1. 방 정보 가져오기 (클래스 방 대응)
		    ChatRoomDTO room = chatMapper.selectRoomByIdx(roomIdx, myIdx);
		    if (room == null) {
		        try {
					room = companyMapper.getClassRoom(roomIdx);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    if (room == null) return "";
		    
		    chatMapper.resetUnread(roomIdx, myIdx);
		    // 2. 저장할 때와 동일한 규칙으로 파일 경로 찾기
		    Path filePath = resolveLegacyChatFilePath(room);
		    if (filePath == null || !Files.exists(filePath)) {
		        return ""; // 파일이 없으면 빈 내용
		    }

		    // 3. 파일 읽어서 리턴
		    try {
		        return Files.readString(filePath, StandardCharsets.UTF_8);
		    } catch (IOException e) {
		        // UTF-8 실패 시 다른 인코딩 시도 (선택 사항)
		        return "";
		    }
		}
	 
	@Override
	public List<Map<String, Object>> fieldTeg() throws Exception{
		
		return companyMapper.fieldTag();
	}
	@Override
	public List<Map<String, Object>> regionTeg() throws Exception{
		
		return companyMapper.regionTag();
	}
	@Override
	public List<Map<String, Object>> getRegionTag(int idx) throws Exception {
		
		return companyMapper.getRegionTag(idx);
	}
	public List<Map<String,Object>> getPayHistory(int user_idx) throws Exception{
		return companyMapper.getPayHistory(user_idx);
	}
	@Override
	public int joinOk(UsersDTO dto) throws Exception {
		dto.setPassword(com.dacoach.javasecure.JavaDataSecureModule.getSHA256(dto.getPassword()));
		int result=companyMapper.joinOk(dto);
		return  result;

	}
	@Override
	public int getUserIdx(String login_id) throws Exception {
		
		return companyMapper.getUserIdx(login_id);
	}
	@Override
	public int companyInfo(CompanyDTO dto) throws Exception {
		
		return companyMapper.companyInfo(dto);
	}
	public int insertcert(CertDTO dto) throws Exception{
		return companyMapper.insertcert(dto);
	}
	
	public int getCompanyNum(int idx) throws Exception{
		return companyMapper.getCompanyNum(idx);
		
	}
	public int addRegion(CompanyRegionDTO dto) throws Exception{
		return companyMapper.addRegion(dto);
	}
	public List<Map<String,Object>> getMinorField(int idx) throws Exception{
		return companyMapper.getMinorField(idx);
	}
	public int provideOk(CompanyProvideDTO dto) throws Exception{
		return companyMapper.provideOk(dto);
	}
	public CompanyDTO getCompanyInfo(int idx) throws Exception{
		return companyMapper.getCompanyInfo(idx);
	}
	public boolean regionCheck(int idx) throws Exception{
		
		List<CompanyRegionDTO> d=companyMapper.regionCheck(idx);
		boolean check=d==null||d.size()==0?false:true;
		return check;
	}
	
	@Override
	public boolean provideCheck(int idx) throws Exception {
		CompanyProvideDTO d=companyMapper.provideCheck(idx);
		boolean check=d==null?false:true;
		return check;
	}
	
	public List<Map<String,Object>> getCompanyRegion(int idx) throws Exception{
		return companyMapper.getCompanyRegion(idx);
	}
	public Map<String,Object> companyProfile(int member_idx) throws Exception{
		return companyMapper.companyProfile(member_idx);
	}
	public List<Map<String,Object>> getProfileClass(Map<String,Object> map)throws Exception{
		return companyMapper.getProfileClass(map);
	}
	@Override
	public CertDTO getCompanyCert(int user_idx) throws Exception {
		// TODO Auto-generated method stub
		return companyMapper.getCompanyCert(user_idx);
	}
	public Map<String,Object> getCompanyProvide(int company_idx) throws Exception{
		return companyMapper.getCompanyProvide(company_idx);
	}
	public List<CompanyRegionDTO> getCompanyAllRegion(int company_idx) throws Exception{
		return companyMapper.getCompanyAllRegion(company_idx);
	}
	
	public boolean companyUp(CompanyDTO companyDto,CompanyProvideDTO provideDto) throws Exception{
		boolean check=false;
		int comResult=companyMapper.companyUp(companyDto);
		if(comResult>0)check=true;
		
		if(provideDto.getMinor_field_idx()!=0) {
		int proResult=companyMapper.provideUp(provideDto);
		if(proResult>0)check=true;
		}
		
		return check;
	}
	public int regionDel(CompanyRegionDTO dto) throws Exception{
		return companyMapper.regionDel(dto);
	}
	public List<Map<String,Object>> getCoachList() throws Exception{
		return companyMapper.getCoachList();
	}
	public List<ClassDTO> getClass(int user_idx) throws Exception{
		return companyMapper.getClass(user_idx);
	}
	public List<ClassEnrollmentDTO> getUserClass(int class_idx) throws Exception{
		return companyMapper.getUserClass(class_idx);
	}
	public ChatRoomDTO getClassRoom(int roomIdx) throws Exception{
		return companyMapper.getClassRoom(roomIdx);
	}
}
