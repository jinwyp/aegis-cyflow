# aegis-cyflow
cy flow



```
  ///////////////////////////////////////////////////////////////////////////////////////
  //      \ ----------------------> VoidEdge
  //      V0
  //       \ ---------------------> A(data_A) B(data_B) C(data_C)   E1  : data gather
  //       V1
  //         \--------------------> [D, E, F](data_DEF)             E2  : data gather
  //         v2
  //          \-------------------> [D, E, F]                       E3  : data gather
  //          V3
  //           \------------------> [G, H, K]                       E4  : data gather
  //            V4
  //           /  \---------------> [UA1, UA2](task_A)              E5  : user gather
  //          V7   V5 
  //         /      \-------------> [UB1, UB2](task_B)              E6  : user gather
  //        V8       V6
  ///////////////////////////////////////////////////////////////////////////////////////
```

## to

1. add flowType to message to support        todo   done
2. add userType to message                   todo   maybe not needed
   just like flowType  
   to support multiple user-types  
3. change serialization to custom            todo
4. change serialization to protobuf          todo
5. angular2 + D3 for operation visualization todo
6. generic type support for DataPoint        todo
7. cluster sharding                          todo
8. add swagger documentation                 todo 
9. rest api                                  todo
10. schema migration                         todo
11. neo4j for permission management          todo
12. 



