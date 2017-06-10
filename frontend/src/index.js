import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Route, Link } from "react-router-dom"

import BankIndex from "./bank-ui/BankIndex";
import NewsIndex from "./news-ui/NewsIndex";
import SocialIndex from "./social-ui/SocialIndex";


class Index extends React.Component {

    render() {
        return (
            <Router>
                <div>
                    <div>
                        <Link to="/bank">Bank</Link>
                        <Link to="/news">News</Link>
                        <Link to="/social">Social</Link>
                    </div>

                    <Route exact path="/bank" component={BankIndex} />
                    <Route exact path="/news" component={NewsIndex} />
                    <Route exact path="/social" component={SocialIndex} />
                </div>
            </Router>
        )
    }

}

ReactDOM.render(<Index />, document.getElementById("reactApp"));