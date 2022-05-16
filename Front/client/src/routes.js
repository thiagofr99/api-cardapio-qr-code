import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';

import Login from './pages/Login';
import Empresa from './pages/Empresa';
import Usuario from './pages/Usuario';
import Manager from './pages/Manager';


export default function Routes() {
    return (
        <BrowserRouter>
            <Switch>
                <Route path="/" exact component={Login}/>
                <Route path="/empresa" component={Empresa}/>                
                <Route path="/usuario" component={Usuario}/>                
                <Route path="/manager" component={Manager}/>                
            </Switch>
        </BrowserRouter>
    );
}