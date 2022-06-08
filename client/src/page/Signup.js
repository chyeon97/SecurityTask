import React, {useEffect, useState} from 'react';
import {
  Button,
  Grid,
  TextField,
  FormControl,
  RadioGroup,
  FormControlLabel,
  Radio
} from "@mui/material";
import {useHistory} from "react-router-dom";
import axios from "axios";

export const Signup = () => {
  const history = useHistory();
  const AuthInfo = {"user": "ROLE_USER", "admin": "ROLE_ADMIN"};


  const [info, setInfo] = useState({
    "username"    : "",
    "password": "",
    "auth" : "user"
  });

  const onHandleInfo = (e) => {
    setInfo({...info, [e.target.id]: e.target.value});
  }

  const postEnroll = () => {
    axios.post("http://52.9.214.153:8080/signup", {
      username    : info.username,
      password: info.password,
      roles    : AuthInfo[info.auth],

    }).then(function (res) {
      if (res.status === 200) {
        history.push("/");
      }
    }).catch(function (err) {
      console.log(err)
    });
  }

  useEffect(() => {
    console.log(info)
  }, [info])

  return (
    <Grid contianer={"true"} direction={"column"} alignItems={"center"} justifyContent={"center"}
          display={"flex"}
          style={{border: "1px solid #00000025", borderRadius: 5, padding: 20, width: 500}}>
      <h1> 회원가입 </h1>
      <FormControl>
        <RadioGroup
          id={"auth"}
          row
          aria-labelledby="demo-radio-buttons-group-label"
          defaultValue="user"
          name="radio-buttons-group"
          onChange={onHandleInfo}
        >
          <FormControlLabel id={"auth"} value="user" control={<Radio id={"auth"}/>} label="일반 사용자"/>
          <FormControlLabel id={"auth"} value="admin" control={<Radio id={"auth"}/>} label="관리자"/>
        </RadioGroup>
      </FormControl>

      <Grid item style={{width: "80%"}}>
        <Grid>
          <TextField id={"username"} label={"username"} variant={"outlined"} style={{width: "100%"}}
                      onChange={onHandleInfo}></TextField>
        </Grid>

        <Grid>
          <TextField type={"password"} id={"password"} label={"password"} variant={"outlined"}
                    style={{width: "100%"}} onChange={onHandleInfo}></TextField>
        </Grid>

        <Grid display={"flex"} alignItems={"center"} justifyContent={"flex-end"}
              style={{width: "100%", padding: 10}}>
          <Button variant="contained" onClick={postEnroll}>가입</Button>
          <Button color={"secondary"} variant="contained" onClick={() => {
            history.push("/")
          }}>취소</Button>
        </Grid>

      </Grid>
    </Grid>
  )
}
