import React,{useState,useEffect} from 'react';
import {Grid, Button, TextField, Typography} from '@mui/material';
import {useHistory} from "react-router-dom";
import axios from "axios";

export const Signin = () => {
  const history = useHistory();
  useEffect(()=>{
    localStorage.clear();
  },[])


  const [info, setInfo] = useState({
    username:"",
    password:""
  })


  const onChangeInfo = (e) =>{
    setInfo({...info, [e.target.id]:e.target.value});
  }

  const postLogin = () =>{
    //http://localhost:8080/login
    
    axios.post("http://52.9.214.153:8080/login",{
      username:info.username,
      password:info.password
    }).then(function (res){
      if(res.data.resCode === 1){
        alert("존재하지 않는 사용자 정보입니다.")
      }else{
        const access = res.headers.authorization;
        const refresh = res.headers.refresh_token;
        localStorage.setItem("ACCESS_TOKEN", access);
        localStorage.setItem("REFRESH_TOKEN",refresh);

        history.push("/api/user");
      }
    }).catch(function (err){
      console.log(err);
    });
  }


  return (
    <Grid contianer={"true"} direction={"column"} alignItems={"center"} justifyContent={"center"} display={"flex"}
          style={{border: "1px solid #00000025", borderRadius: 5, padding: 20, width: 500}}>
      <h1> 로그인</h1>
      <Grid item style={{width:"80%"}}>
        <Grid >
          <TextField id={"username"} label={"username"} variant={"outlined"} style={{width: "100%"}} onChange={(e)=> onChangeInfo(e)}></TextField>
        </Grid>
        <Grid>
          <TextField type={"password"} id={"password"} label={"password"} variant={"outlined"} onChange={(e)=>onChangeInfo(e)}
                style={{width: "100%"}}></TextField>
        </Grid>

        <Grid display={"flex"} alignItems={"center"} justifyContent={"flex-end"} style={{width:"100%", padding:10}}>
          <Button variant="contained" onClick={postLogin}>로그인</Button>
          <Button color={"secondary"} variant="contained" onClick={() => {
            history.push("/signup")
          }}>회원가입</Button>
        </Grid>
      </Grid>
    </Grid>

  );

}
