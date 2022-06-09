import React, {useEffect} from "react";
import {Button} from "@mui/material";
import axios from "axios";
import {useHistory} from "react-router-dom";

export const Main = () => {
  const history = useHistory();

  const reloadTest = () => {
    const config = {
      headers: {
        Authorization: localStorage.getItem("ACCESS_TOKEN")
      },
    }

    axios.get("http://52.9.214.153:8080/api/user", config).then((res) => {
      if(res.data && res.data.resCode === 1){
        alert("새로고침을 시도하세요")
        getRefreshToken();
      }
    }).catch((err) => {
      console.log(err)
    })

  }

  const getMapping = () => {
    const config = {
      headers: {
        Authorization: localStorage.getItem("ACCESS_TOKEN")
      },
    }
    axios.get("http://52.9.214.153:8080/api/admin", config).then((res) => {
      res.status === 200 && history.push("/api/admin")
    }).catch((err) => {
      alert("관리자 권한이 없습니다.")
    })
  }


  const getRefreshToken = () =>{
    const config = {
      headers: {
        Authorization: localStorage.getItem("ACCESS_TOKEN"),
        Refresh_token: localStorage.getItem("REFRESH_TOKEN")
      },
    }

    axios.get("http://52.9.214.153:8080/api/user", config).then((res) => {
      console.log(res)
      if(res.data && res.data.resCode === 2) history.push("/")

      localStorage.setItem("ACCESS_TOKEN", res.headers.authorization);
    }).catch((err) => {
      console.log(err)
    })


  }


  useEffect(()=>{
    reloadTest();

  },[])

  return (
    <div>
      <h1>MAIN</h1>
      <Button color={"secondary"} variant="contained" onClick={getMapping}>관리자 페이지 이동</Button>
    </div>
  )


}
