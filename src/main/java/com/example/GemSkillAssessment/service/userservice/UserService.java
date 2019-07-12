package com.example.GemSkillAssessment.service.userservice;

import com.example.GemSkillAssessment.dao.FormRepository;
import com.example.GemSkillAssessment.dao.JWTRepository;
import com.example.GemSkillAssessment.dao.PermissionRepository;
import com.example.GemSkillAssessment.dao.UserRepository;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.enumerted.EJobTitle;
import com.example.GemSkillAssessment.enumerted.EPermission;
import com.example.GemSkillAssessment.error.DuplicateEntryException;
import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.error.NotFoundException;
import com.example.GemSkillAssessment.error.NotImplementedException;
import com.example.GemSkillAssessment.model.*;
import com.example.GemSkillAssessment.model.dto.EmployeeDTO;
import com.example.GemSkillAssessment.service.Verify;
import com.example.GemSkillAssessment.service.email.SendEmailService;
import com.example.GemSkillAssessment.service.period.PeriodService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements IUserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);
    @PersistenceContext
    EntityManager em;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTRepository jwtRepository;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Verify verify;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private PeriodService periodService;

    public static List<User> getUserExcel(byte[] data) throws IOException, ParseException {
        List<User> users = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        InputStream excelFile = new ByteArrayInputStream(data);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        DataFormatter fmt = new DataFormatter();
        Iterator<Row> iterator = datatypeSheet.iterator();
        Row firstRow = iterator.next();
        Row secondRow = iterator.next();
        Row thirdRow = iterator.next();
        Row fourthRow = iterator.next();
        Cell firstCell = fourthRow.getCell(0);
        int i = 0;

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            if (fmt.formatCellValue(currentRow.getCell(0)) == "") {
                continue;
            }
            User user = new User();
            user.setName(fmt.formatCellValue(currentRow.getCell(0)));
            user.setIdEmployee(fmt.formatCellValue(currentRow.getCell(1)));
            try {
                user.setEJobTitle(EJobTitle.valueOf(fmt.formatCellValue(currentRow.getCell(2))));
            } catch (IllegalArgumentException n) {
                throw new NotAcceptableException("JobTitle can't be null or wrong format");
            }
            try {
                user.setJoiningDate(formatter.parse((fmt.formatCellValue(currentRow.getCell(3)))));
            } catch (ParseException p) {
                throw new NotAcceptableException("JoiningDate can't be null or wrong format");
            }
            user.setSupervisedName(fmt.formatCellValue(currentRow.getCell(4)));
            user.setEmail(fmt.formatCellValue(currentRow.getCell(5)));
            users.add(user);
            i++;
        }
        workbook.close();


        return users;
    }

    @Override
    public void inviteListUser(List<String> ids) {
        sendEmailService.sendEmailAll(ids);
    }

    @Override
    public void updateDefaultFieldUser(String idEmployee, String jobTitle, String name, String supervisedName, int id) {
        //userRepository.updateDefaultFieldUser(jobTitle, name, supervisedName, id);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setTokenVerify(null);
        Set<Permission> permissions = user.getPermissions();
        permissions.add(permissionRepository.findByType(EPermission.FILL));
        userRepository.save(user);
    }

    @Override
    public void deactiveUser(String id) {
        User user = userRepository.findByIdEmployee(id);
        if (user.isEnabled()) user.setEnabled(false);
        else user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<User> importUser(byte[] data) throws IOException, ParseException, DuplicateEntryException {
        List<User> usersExcel = UserService.getUserExcel(data);
        List<User> userDatabase = (List<User>) userRepository.findByIsAdminFalse();
        Map<String, User> userEmail = userDatabase.stream().collect(Collectors.toMap(User::getEmail, user -> user));
        Map<String, User> userID = userDatabase.stream().collect(Collectors.toMap(User::getIdEmployee, user -> user));
        verify.checkDataSource(usersExcel, userDatabase);
        for (User user : usersExcel) {
//            HashSet<Role> roles = new HashSet<>();
//            roles.add(roleRepository.findByName("ROLE_MEMBER"));
            user.setAdmin(false);
            if (userEmail.containsKey(user.getEmail())) {
                // Case update
                User temp = userEmail.get(user.getEmail());
                if (!user.getIdEmployee().equalsIgnoreCase(temp.getIdEmployee()) && userID.containsKey(user.getIdEmployee())) {
                    throw new DuplicateEntryException();
                }
                int id = temp.getId();
                //BeanUtils.copyProperties(user, temp);
                temp.setName(user.getName());
                temp.setIdEmployee(user.getIdEmployee());
                temp.setSupervisedName(user.getSupervisedName());
                temp.setJoiningDate(user.getJoiningDate());
                temp.setEmail(user.getEmail());
                temp.setId(id);
                //temp.setSupervisedId(user.getSupervisedId());
            } else {
                // Case insert
                if (userID.containsKey(user.getIdEmployee())) {
                    throw new DuplicateEntryException();
                } else {
                    userRepository.save(user);
                }
            }
        }
        return userDatabase;
    }

    @Override
    public Iterable<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Integer findNameById(String name) {
        return userRepository.findIdByName(name);
    }

 /*   @Override
    @Transactional
    public void updateSupervisedId() {
        List<User> usersDatabase = (List<User>) userRepository.findAll();
        System.out.println("Number Employee Database: " + usersDatabase.size());
        System.out.println("First user:" + usersDatabase.get(0).getName() + "    " + usersDatabase.get(0).getEmail());
        for (User user : usersDatabase) {
            //FIXME: Reduce connect to DB please
            if (userRepository.findIdByName(user.getName()) == null) throw new UsernameNotFoundException();
            if (user.getId() > 5 && userRepository.findIdByName(user.getSupervisedName()) > 0)
                System.out.println("Id number of supervised: " + userRepository.findIdByName(user.getSupervisedName()));
        }

    }*/

    public void getListIdUserAndSuperviser() {
        List<User> usersDatabase = (List<User>) userRepository.findAll();
        Map<Integer, Integer> userMap = new HashMap<>();
        for (User user : usersDatabase) {
            userMap.put(user.getId(), userRepository.findIdByName(user.getSupervisedName()));
        }


    }

    @Override
    public List<User> getListEmployeeFromSupervisedId(Integer id, List<User> users) {
        // List<User> users = (List<User>) userRepository.findAll();
        List<User> supervisees = new ArrayList<>();
        if (userRepository.findById(id) == null) throw new NotFoundException("User not found");
        for (User user : users) {
            if (user.getSupervisedId() != 0 && user.getSupervisedId() == userRepository.findById(id).getId()) {
                supervisees.add(user);
            }
        }
        return supervisees;
    }

    @Override
    public List<User> getListEmployeeFromSupervisedEmail(String email) {
        List<User> users = (List<User>) userRepository.findAll();
        List<User> supervisees = new ArrayList<>();
        if (userRepository.findByEmail(email) == null) throw new NotFoundException("User not found");
        for (User user : users) {
            if (user.getSupervisedId() != 0 && user.getSupervisedId() == userRepository.findByEmail(email).getId()) {
                supervisees.add(user);
            }
        }
        return supervisees;
    }

    public void updateSuperviser(Integer idSupervisee, Integer idSuperviser) {
        if (idSupervisee == idSuperviser) {
            logger.info("Id Employee and Superviser must be different");
            throw new NotAcceptableException("Id Employee and Superviser must be different");
        }
        if (userRepository.findById(idSupervisee) == null || userRepository.findById(idSuperviser) == null)
            throw new NotFoundException("User not found");

        User supervisee = userRepository.findById(idSupervisee);
        User superviser = userRepository.findById(idSuperviser);
        supervisee.setSupervisedName(superviser.getName());
        supervisee.setSupervisedId(superviser.getId());
        userRepository.save(supervisee);
    }

    public void updateJobtitle(Integer id, String eJobTitle) {
        User userUpdate = userRepository.findById(id);
        try {
            userUpdate.setEJobTitle(EJobTitle.valueOf(eJobTitle));
        } catch (IllegalArgumentException n) {
            throw new NotAcceptableException("JobTitle can't be null or wrong format");
        }

        userRepository.save(userUpdate);
    }

    @Override
    public void checkReview() {
        Set<Integer> idSuperviser = new HashSet<>();
        List<User> users = (List<User>) userRepository.findAll();
        for (User user : users) {
            System.out.println(user.getSupervisedId());
            idSuperviser.add(user.getSupervisedId());
        }
        System.out.println(idSuperviser);
        users = users.stream().map(item -> {
            if (idSuperviser.contains(item.getId())) {
                System.out.println("la superviser");
                //item.setReview(true);
                return item;
            } else {
                System.out.println(item.getId());
                return item;
            }

        }).collect(Collectors.toList());
        userRepository.saveAll(users);
    }

    public void authEdit(String id) {
        User user = userRepository.findByIdEmployee(id);
        Permission permission = permissionRepository.findByType(EPermission.EDIT);
        Set<Permission> permissionUserSet = user.getPermissions();

        if (!this.checkFillEdit(permissionUserSet, EPermission.FILL)) {
            if (this.checkFillEdit(permissionUserSet, EPermission.EDIT)) permissionUserSet.remove(permission);
            else permissionUserSet.add(permission);
            user.setPermissions(permissionUserSet);
        } else throw new NotImplementedException(EError.FORM_IS_NOT_FINISH.getLabel());

        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getUserAth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName());
    }

    public boolean hasRole(String roleName) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }

    public void saveJwt(JWT jwtLock1) {
        jwtRepository.save(jwtLock1);
    }

    public User findByIdAndSupervisedId(Integer id, int id1) {
        User user = userRepository.findByIdAndSupervisedId(id, id1);
        if (user == null) {
            throw new NotFoundException("Can't search supervisor!");
        }
        return user;
    }

    public User findById(Integer id) {
        return userRepository.findById(id);
    }


    public Set<Integer> getSetSuperviserId(List<User> users) {
        Set<Integer> setInteger = new HashSet<>();
        for (User user : users) {
            if (user.getSupervisedId() != 0) {
                setInteger.add(user.getSupervisedId());
            }
        }
        return setInteger;
    }

    @Override
    public void importReview() {
        List<User> users = (List<User>) userRepository.findByIsAdminFalse();

        for (User user : users) {
            if (!"".equals(user.getSupervisedName()) && user.getSupervisedName() != null) {
                userRepository.updateSupervisedId(userRepository.findIdByName(user.getSupervisedName()), user.getId());
            }
            userRepository.saveAll(users);
            if (userService.getSetSuperviserId(users).contains(user.getId())) {
                System.out.println(user.getName());
                Set<Permission> permissionSet = user.getPermissions();
                if (permissionSet == null) permissionSet = new HashSet<>();
                permissionSet.add(permissionRepository.findByType(EPermission.REVIEW));
                userRepository.save(user);
            }
        }
    }

    public boolean checkFillEdit(Set<Permission> permissions, EPermission ePermission) {
        for (Permission permission : permissions) {
            if (permission.getType() == ePermission) {
                return true;
            }
        }
        return false;
    }

    public Boolean checkPassword(String passwordUser, String passwordOld) {
        return passwordEncoder.matches(passwordOld, passwordUser);
    }

    public void remindUser(Integer id) {
        User user = userRepository.findById(id);
        if (user != null) sendEmailService.remindUser(user);
        else throw new NotFoundException(EError.USER_NOT_FOUND.getLabel());
        Period period = periodService.checkPeriodStart(null);
        Form form = formRepository.findByEnableIsTrueAndPeriodAndUser(period, user);
        form.setRemindReviewSelf(true);
    }

    public void remindReviewUser(Integer id) {
        User user = userRepository.findById(id);
        User userReview = userRepository.findById(user.getSupervisedId());
        if (user != null && userReview != null) sendEmailService.remindReviewUser(user, userReview);
        else throw new NotFoundException(EError.USER_NOT_FOUND.getLabel());
        Period period = periodService.checkPeriodStart(null);
        Form form = formRepository.findByEnableIsTrueAndPeriodAndUser(period, user);
        form.setRemindReviewUser(true);
    }

    public void setPermission(List<EmployeeDTO> employeeDTOs) {
        for (EmployeeDTO employeeDTO : employeeDTOs) {
            if (employeeDTO.getPermissions().contains(permissionRepository.findByType(EPermission.EDIT))) {
                employeeDTO.setEdit(true);
            }
            if (employeeDTO.getPermissions().contains(permissionRepository.findByType(EPermission.FILL))) {
                employeeDTO.setFill(true);
            }
            if (employeeDTO.getPermissions().contains(permissionRepository.findByType(EPermission.REVIEW))) {
                employeeDTO.setReview(true);
            }
        }
    }

    public void setPermission(EmployeeDTO employeeDTO) {

        if (employeeDTO.getPermissions().contains(permissionRepository.findByType(EPermission.EDIT))) {
            employeeDTO.setEdit(true);
        }
        if (employeeDTO.getPermissions().contains(permissionRepository.findByType(EPermission.FILL))) {
            employeeDTO.setFill(true);
        }
        if (employeeDTO.getPermissions().contains(permissionRepository.findByType(EPermission.REVIEW))) {
            employeeDTO.setReview(true);

        }
    }

    public void setLeft(Integer id) {
        User user = userRepository.findById(id);
        if(!user.isLeft()) user.setLeft(true);
        else user.setLeft(false);
        userRepository.save(user);
    }

//    public void inviteUser(String id) {
//        User user = userRepository.findByIdEmployee(id);
//        String token = UUID.randomUUID().toString();
//        try {
//            user.setTokenVerify(token);
//            user.setInvited(true);
//            userRepository.save(user);
//            sendEmailService.sendEmail(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new NotImplementedException("Unable to email invitations to " + user.getEmail());
//        }
//    }

}
