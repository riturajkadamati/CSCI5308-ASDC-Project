package com.CanadaEats.group13.authentication.repository;

import com.CanadaEats.group13.authentication.dto.UserLoginDto;
import com.CanadaEats.group13.authentication.model.response.UserLoginResponseModel;
import com.CanadaEats.group13.authentication.model.response.UserDetailsResponseModel;
import com.CanadaEats.group13.authentication.dto.UserDetailsDto;
import com.CanadaEats.group13.database.IDatabaseConnection;
import com.CanadaEats.group13.utils.ApplicationConstants;
import com.CanadaEats.group13.utils.PasswordEncoderDecoder;
import org.springframework.beans.BeanUtils;

import java.sql.*;

public class UserRepository implements IUserRepository{

    IDatabaseConnection databaseConnection;
    //DatabaseConnection databaseConnection;
    Connection connection;
    Statement statement;
    ResultSet userResult;
    public UserRepository(IDatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public UserDetailsResponseModel registerUser(UserDetailsDto userDetails)
    {
        UserDetailsResponseModel userResponse = new UserDetailsResponseModel();
        try
        {
            //databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getDatabaseConnection();
            statement = connection.createStatement();
            String getUser = "select * from User where UserName = '" + userDetails.getUserName() + "' and Status = 1";
            userResult = statement.executeQuery(getUser);

            if(userResult.next() == false)
            {
                String insertUser = "insert into User (UserId, FirstName, LastName, EmailId, UserName, Password, MobileNumber, Gender, BirthDate, Address, City, Province, Country, PostalCode, Status, Role_RoleId) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement prepStat=connection.prepareStatement(insertUser);
                prepStat.setString(1, userDetails.getUserId());
                prepStat.setString(2, userDetails.getFirstName());
                prepStat.setString(3, userDetails.getLastName());
                prepStat.setString(4, userDetails.getEmailId());
                prepStat.setString(5, userDetails.getUserName());
                prepStat.setString(6, userDetails.getPassword());
                prepStat.setString(7, userDetails.getMobileNumber());
                prepStat.setString(8, userDetails.getGender());
                prepStat.setDate(9, userDetails.getBirthDate());
                prepStat.setString(10, userDetails.getAddress());
                prepStat.setString(11, userDetails.getCity());
                prepStat.setString(12, userDetails.getProvince());
                prepStat.setString(13, userDetails.getCountry());
                prepStat.setString(14, userDetails.getPostalCode());
                prepStat.setInt(15, userDetails.getStatus());
                prepStat.setString(16, userDetails.getRoleId());
                int result = prepStat.executeUpdate();

                if(result > 0)
                {
                    BeanUtils.copyProperties(userDetails, userResponse);
                    System.out.println("Success : UserRepository - registerUser()");
                }
                else
                {
                    System.out.println("Failure : UserRepository - registerUser()");
                }
            }
            else{
                System.out.println("Failure : Already User Present with this UserName");
            }

        }
        catch (Exception ex) {
            System.out.println("Exception : UserRepository - registerUser()");
            System.out.println(ex);
            System.out.println(ex.getMessage());
        }
        finally{
            try{
                connection.close();
                statement.close();
                userResult.close();
            }
            catch (Exception ex){
                System.out.println("Exception : UserRepository - Closing database connection in registerUser()");
            }
        }

        return userResponse;
    }

    public UserLoginResponseModel loginUser(UserLoginDto userLoginDto)
    {
        UserLoginResponseModel userLoginResponseModel = new UserLoginResponseModel();

        try{
            //databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getDatabaseConnection();
            statement = connection.createStatement();
            String getUser = "select * from User where UserName = '" + userLoginDto.getUserName() + "' and Status = 1";
            userResult = statement.executeQuery(getUser);
            System.out.println("SIZE:- " + userResult.getFetchSize());

            if (userResult.next() == false)
            {
                System.out.println("username not found");
            }
            else{
                try
                {
                    System.out.println("Got username successfully from Database");

                    String decryptedPassword = PasswordEncoderDecoder.getInstance().decrypt(userResult.getString(ApplicationConstants.USER_PASSWORD_COLUMN));
                    System.out.println("DecryptedPassword: " + decryptedPassword);
                    System.out.println("UserPassword: " + userLoginDto.getPassword());
                    if(decryptedPassword.equals(userLoginDto.getPassword()))
                    {
                        System.out.println("Got username and password successfully from Database");
                        userLoginResponseModel.setRoleId(userResult.getString(ApplicationConstants.USER_ROLEID_COLUMN));
                        userLoginResponseModel.setUserName(userResult.getString(ApplicationConstants.USER_USERNAME_COLUMN));
                        return userLoginResponseModel;
                    }
                    else{
                        System.out.println("Password not matched");
                        return userLoginResponseModel;
                    }
                }
                catch (Exception ex)
                {
                    System.out.println("Exception: In Password Encoding In User Registration");
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println("Exception : UserRepository - loginUser()");
            System.out.println(ex);
            System.out.println(ex.getMessage());
        }
        finally{
            try{
                connection.close();
                statement.close();
                userResult.close();
            }
            catch (Exception ex){
                System.out.println("Exception : UserRepository - Closing database connection in registerUser()");
            }
        }
        return userLoginResponseModel;
    }
}
