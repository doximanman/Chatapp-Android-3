function Input({ description }) {
    return (
        <div className="col-11 row ">
            <label className={description.labelClass}>{description.ins}</label>
            <div className={description.divClass}>
                <input value={description.value} onChange={description.onChange} type={description.type} className={description.className} id={description.id} name={description.name} required></input>
            </div>
        </div>
    );
}

export default Input;