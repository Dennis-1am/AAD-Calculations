import logo from '../images/logo-removebg-preview.png';
import './Navbar.css'

const logoLabel = () => {
    return (
        <div className="logo">
            <img src={logo} className="App-logo" alt="logo" />
        </div>
    )
}

function Navbar() {

    return (
        <nav>
            <ul id="navbar">
                <li><a href="https://www.signetjewelers.com/our-home/default.aspx">{logoLabel()}</a></li>
                <li>API Information</li>
                <li>Contact Developer</li>
            </ul>
        </nav>
    )
    
}

export default Navbar